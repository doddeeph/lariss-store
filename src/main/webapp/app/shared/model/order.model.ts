import dayjs from 'dayjs';
import { ICustomer } from 'app/shared/model/customer.model';
import { OrderStatus } from 'app/shared/model/enumerations/order-status.model';

export interface IOrder {
  id?: number;
  status?: keyof typeof OrderStatus | null;
  orderDate?: dayjs.Dayjs | null;
  shippingAddress?: string | null;
  customer?: ICustomer | null;
}

export const defaultValue: Readonly<IOrder> = {};
