package com.mmall.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CartMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Cart;
import com.mmall.pojo.Product;
import com.mmall.service.ICartService;
import com.mmall.util.BigDecimalUtil;
import com.mmall.util.Const;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service("iCartService")
public class CartServiceImpl implements ICartService{

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private ProductMapper productMapper;

    public ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count) {
        if (productId == null || count == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARUMENT.getCode(), ResponseCode.ILLEGAL_ARUMENT.getDesc());
        }
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart == null) {
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            newCart.setProductId(productId);
            newCart.setChecked(Const.Cart.CHECKED);
            newCart.setQuantity(count);
            cartMapper.insert(newCart);
        } else {
            count = cart.getQuantity() + count;
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKeySelective(cart);
        }
        return this.list(userId);
    }

    public ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count) {
        if (productId == null || count == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARUMENT.getCode(), ResponseCode.ILLEGAL_ARUMENT.getDesc());
        }
        Cart cart = cartMapper.selectByUserIdAndProductId(userId, productId);
        if (cart != null) {
            cart.setQuantity(count);
        }
        cartMapper.updateByPrimaryKeySelective(cart);
        return this.list(userId);
    }

    public ServerResponse<CartVo> deleteProducts(Integer userId, String productIds) {
        List<String> productIdList = Splitter.on(",").splitToList(productIds);
        if (CollectionUtils.isEmpty(productIdList)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARUMENT.getCode(), ResponseCode.ILLEGAL_ARUMENT.getDesc());
        }
        cartMapper.deleteByUserIdAndProductIds(userId, productIdList);
        return this.list(userId);
    }

    public ServerResponse<CartVo> selectOrUnSelect(Integer userId, Integer checked, Integer productId) {
        cartMapper.checkOrUncheckProduct(userId, checked, productId);
        return this.list(userId);
    }

    public ServerResponse<Integer> getProductCount(Integer userId) {
        Integer count = cartMapper.selectProductCountByUserId(userId);
        return ServerResponse.createBySuccess(count);
    }

    public ServerResponse<CartVo> list(Integer userId) {
        return ServerResponse.createBySuccess(this.getCartVoLimit(userId));
    }


    private CartVo getCartVoLimit(Integer userId) {
        CartVo cartVo = new CartVo();
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        List<CartProductVo> cartProductVoList = Lists.newArrayList();

        BigDecimal cartTotalPrice = new BigDecimal("0");

        if (CollectionUtils.isNotEmpty(cartList)) {
            for (Cart cartItem : cartList) {
                CartProductVo vo = new CartProductVo();
                vo.setId(cartItem.getId());
                vo.setUserId(cartItem.getUserId());
                vo.setProductId(cartItem.getProductId());
                vo.setProductChecked(cartItem.getChecked());

                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if (product != null) {
                    vo.setProductName(product.getName());
                    vo.setProductPrice(product.getPrice());
                    vo.setProductStatus(product.getStatus());
                    vo.setProductSubtitle(product.getSubtitle());
                    vo.setProductStock(product.getStock());
                    vo.setProductMainImage(product.getMainImage());

                    int buyLimitCount = 0;
                    if (cartItem.getQuantity() <= product.getStock()) {
                        // 库存充足
                        buyLimitCount = cartItem.getQuantity();
                        vo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    } else {
                        // 库存不足
                        buyLimitCount = product.getStock();
                        cartItem.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cartItem);
                    }
                    vo.setQuantity(buyLimitCount);
                    vo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue(), vo.getQuantity().doubleValue()));
                }

                if (cartItem.getChecked() == Const.Cart.CHECKED) {
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(), vo.getProductTotalPrice().doubleValue());
                }
                cartProductVoList.add(vo);
            }
        }
        cartVo.setCartProductVoList(cartProductVoList);
        cartVo.setAllChecked(this.getAllCheckedStatus(userId));
        cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        return cartVo;
    }

    private boolean getAllCheckedStatus(Integer userId) {
        return cartMapper.selectCheckedStatusByUserId(userId) == 0;
    }

}
