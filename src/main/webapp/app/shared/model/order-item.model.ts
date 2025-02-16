import { IProductVariant } from 'app/shared/model/product-variant.model';
import { IOrder } from 'app/shared/model/order.model';

export interface IOrderItem {
  id?: number;
  quantity?: number | null;
  price?: number | null;
  productVariant?: IProductVariant | null;
  order?: IOrder | null;
}

export const defaultValue: Readonly<IOrderItem> = {};
