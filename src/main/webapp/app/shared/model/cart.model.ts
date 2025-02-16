import dayjs from 'dayjs';
import { ICustomer } from 'app/shared/model/customer.model';

export interface ICart {
  id?: number;
  createdDate?: dayjs.Dayjs | null;
  customer?: ICustomer | null;
}

export const defaultValue: Readonly<ICart> = {};
