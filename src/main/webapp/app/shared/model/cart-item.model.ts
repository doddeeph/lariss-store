import { IProductVariant } from 'app/shared/model/product-variant.model';
import { ICart } from 'app/shared/model/cart.model';

export interface ICartItem {
  id?: number;
  quantity?: number | null;
  price?: number | null;
  productVariant?: IProductVariant | null;
  cart?: ICart | null;
}

export const defaultValue: Readonly<ICartItem> = {};
