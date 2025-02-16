import { ICategory } from 'app/shared/model/category.model';
import { CurrencyCode } from 'app/shared/model/enumerations/currency-code.model';

export interface IProduct {
  id?: number;
  productName?: string | null;
  description?: string | null;
  currencyCode?: keyof typeof CurrencyCode | null;
  category?: ICategory | null;
}

export const defaultValue: Readonly<IProduct> = {};
