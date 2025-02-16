import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Category from './category';
import Product from './product';
import ProductVariant from './product-variant';
import Cart from './cart';
import CartItem from './cart-item';
import Order from './order';
import OrderItem from './order-item';
import Customer from './customer';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="category/*" element={<Category />} />
        <Route path="product/*" element={<Product />} />
        <Route path="product-variant/*" element={<ProductVariant />} />
        <Route path="cart/*" element={<Cart />} />
        <Route path="cart-item/*" element={<CartItem />} />
        <Route path="order/*" element={<Order />} />
        <Route path="order-item/*" element={<OrderItem />} />
        <Route path="customer/*" element={<Customer />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
