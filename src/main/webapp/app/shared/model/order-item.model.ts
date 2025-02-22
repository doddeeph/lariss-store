import { IOrder } from 'app/shared/model/order.model';
import { IProductVariant } from 'app/shared/model/product-variant.model';

export interface IOrderItem {
  id?: number;
  quantity?: number | null;
  price?: number | null;
  order?: IOrder | null;
  productVariant?: IProductVariant | null;
}

export const defaultValue: Readonly<IOrderItem> = {};
