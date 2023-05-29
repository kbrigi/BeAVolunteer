import { Domain } from "src/app/domain/models/domain.model"

export type Volunteer = { 
    surname: String, 
    firstname: String,
    email: string,
    user: string,
    password: string,
    age: Number,
    description: string,
    gender: string,
    phoneNr: string,
    domains: Domain[],
    volunteered: Boolean
}