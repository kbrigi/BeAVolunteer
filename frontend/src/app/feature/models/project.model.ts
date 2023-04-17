import { Domain } from "src/app/domain/models/domain.model"

export type Project = { 
    project_name: string,
    project_description: string,
    expiration_date: Date,
    domains: Domain[]

}