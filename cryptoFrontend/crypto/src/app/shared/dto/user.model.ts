export interface UserDto {
    userId: string;
    name: string;
    wallets: Map<string, number>
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
