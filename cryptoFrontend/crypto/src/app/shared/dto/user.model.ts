export interface UserDto {
    userId: string;
    name: string;
    wallets: Wallet[];
}

export interface UserLoginDto {
    userId: string;
    password: string;
}

export interface RegisterDto {
    userId: string;
    password: string;
    name: string;
}

export interface Wallet {
    ccy: string;
    amount: number;
}
