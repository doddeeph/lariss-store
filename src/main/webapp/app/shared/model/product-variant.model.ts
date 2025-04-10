import { IProduct } from 'app/shared/model/product.model';

export interface IProductVariant {
  id?: number;
  imageUrl?: string | null;
  price?: number | null;
  color?: string | null;
  processor?: string | null;
  memory?: string | null;
  storage?: string | null;
  screen?: string | null;
  connectivity?: string | null;
  material?: string | null;
  caseSize?: string | null;
  strapColor?: string | null;
  strapSize?: string | null;
  summary?: string | null;
  product?: IProduct | null;
}

export const defaultValue: Readonly<IProductVariant> = {};
