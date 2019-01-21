package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.ICategoryService;
import com.mmall.service.IProductService;
import com.mmall.util.Const;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Service("iProductService")
public class ProductServiceImpl implements IProductService{

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService iCategoryService;

    public ServerResponse<String> saveOrUpdateProduct(Product product) {
        if (product == null) {
            return ServerResponse.createByErrorMessage("参数错误");
        }
        if (StringUtils.isNotBlank(product.getSubImages())) {
            String[] images = product.getSubImages().split(",");
            if (images.length > 0) {
                product.setMainImage(images[0]);
            }
        }

        if (product.getId() != null) {
            int rowCount = productMapper.updateByPrimaryKeySelective(product);
            if (rowCount > 0) {
                return ServerResponse.createBySuccessMessage("更新商品成功");
            }
            return ServerResponse.createByErrorMessage("更新商品失败");
        } else {
            int rowCount = productMapper.insert(product);
            if (rowCount > 0) {
                return ServerResponse.createBySuccessMessage("新增商品成功");
            }
            return ServerResponse.createByErrorMessage("新增商品失败");
        }
    }

    public ServerResponse<String> setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if (rowCount > 0) {
            return ServerResponse.createBySuccessMessage("更改商品销售状态成功");
        }
        return ServerResponse.createByErrorMessage("更改商品销售状态失败");
    }

    public ServerResponse manageProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARUMENT.getCode(), ResponseCode.ILLEGAL_ARUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("商品已下架或删除");
        }

        ProductDetailVo pdv = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(pdv);
    }

    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize) {

        PageHelper.startPage(pageNum, pageSize);

        List<Product> productList = productMapper.selectList();
        List<ProductListVo> productListVoList = Lists.newLinkedList();
        for (Product productItem : productList) {
            ProductListVo vo = assembleProductListVo(productItem);
            productListVoList.add(vo);
        }
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productList);
        return ServerResponse.createBySuccess(pageResult);
    }

    public ServerResponse<PageInfo> searchProductList(String productName, Integer productId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(productName)) {
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> productList = productMapper.selectListByProductNameAndProductId(productName, productId);
        List<ProductListVo> voList = Lists.newArrayList();
        for (Product item : productList) {
            voList.add(assembleProductListVo(item));
        }
        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(voList);
        return ServerResponse.createBySuccess(pageResult);
    }

    private ProductListVo assembleProductListVo(Product product) {
        ProductListVo vo = new ProductListVo();
        vo.setId(product.getId());
        vo.setCategoryId(product.getCategoryId());
        vo.setName(product.getName());
        vo.setMainImage(product.getMainImage());
        vo.setPrice(product.getPrice());
        vo.setSubtitle(product.getSubtitle());
        vo.setStatus(product.getStatus());
        vo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.mmall.com/"));
        return vo;
    }

    private ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo vo = new ProductDetailVo();
        vo.setId(product.getId());
        vo.setCategoryId(product.getCategoryId());
        vo.setName(product.getName());
        vo.setDetail(product.getDetail());
        vo.setStock(product.getStock());
        vo.setPrice(product.getPrice());
        vo.setMainImage(product.getMainImage());
        vo.setSubImages(product.getSubImages());
        vo.setStatus(product.getStatus());
        vo.setSubtitle(product.getSubtitle());
        vo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.mmall.com/"));

        Category category = categoryMapper.selectByPrimaryKey(vo.getCategoryId());
        if (category == null) {
            vo.setParentCategoryId(0);
        }else {
            vo.setParentCategoryId(category.getParentId());
        }

        vo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        vo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));

        return vo;
    }


    public ServerResponse getProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARUMENT.getCode(), ResponseCode.ILLEGAL_ARUMENT.getDesc());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("商品已下架或删除");
        }
        if (product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()) {
            return ServerResponse.createByErrorMessage("商品已下架或删除");
        }

        ProductDetailVo pdv = assembleProductDetailVo(product);
        return ServerResponse.createBySuccess(pdv);
    }

    public ServerResponse<PageInfo> getProductByKeyword(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy) {
        if (StringUtils.isBlank(keyword) && categoryId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARUMENT.getCode(), ResponseCode.ILLEGAL_ARUMENT.getDesc());
        }
        List<Integer> categoryIdList = Lists.newArrayList();
        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && StringUtils.isBlank(keyword)) {
                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVo> list = Lists.newArrayList();
                PageInfo pageResult = new PageInfo(list);
                return ServerResponse.createBySuccess(pageResult);
            }
            categoryIdList = iCategoryService.getCategoryAndDeepChildrenCategory(categoryId).getData();
        }
        if (StringUtils.isNotBlank(keyword)) {
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }

        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(orderBy)) {
            if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
            }
        }
        List<Product> productList = productMapper.selectListByNameAndCategroyIds(StringUtils.isBlank(keyword) ? null : keyword,
                categoryIdList.size() == 0 ? null : categoryIdList);

        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product productItem : productList) {
            ProductListVo vo = assembleProductListVo(productItem);
            productListVoList.add(vo);
        }
        PageInfo result = new PageInfo(productList);
        result.setList(productListVoList);
        return ServerResponse.createBySuccess(result);
    }

}
