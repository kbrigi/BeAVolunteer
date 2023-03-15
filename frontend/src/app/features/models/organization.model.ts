import { Domain } from "./domain.model"

export type Organization = { 
    email: string,
    user: string,
    password: string,
    description: string,
    address: string,
    phoneNr: string,
    website: string,
    logo: string,
    domains: Domain[]
}