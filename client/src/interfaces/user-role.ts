import { RoleTypeEnum } from '@/enums/user-enum'

export interface IRole {
  id: number;
  name: RoleTypeEnum;
  status: string;
  authorities?: IAuthority[];
}

export interface IUser {
  id: string;
  username: string;
  password?: string;
  email: string;
  status: string;
  createTimestamp: number;
  updateTimestamp: number;
  roles: IRole[];
}

export interface IAuthority {
  id: number;
  name: RoleTypeEnum;
  authority: string;
  description: string;
  method: string;
}
