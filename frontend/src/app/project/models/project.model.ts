import { Domain } from "src/app/domain/models/domain.model"
import { User } from "src/app/user/models/user.model"

export type Project = { 
    project_name: string,
    project_description: string,
    expiration_date: Date,
    domains: Domain[],
    creation_date: Date,
    owner: User,
    project_img: String

}