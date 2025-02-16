export interface ICustomer {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  phoneNumber?: string | null;
  emailAddress?: string | null;
  deliveryAddress?: string | null;
}

export const defaultValue: Readonly<ICustomer> = {};
