package edu.bbte.beavolunteerbackend.service;

import edu.bbte.beavolunteerbackend.controller.dto.incoming.ProjectInDTO;
import edu.bbte.beavolunteerbackend.controller.dto.outgoing.ProjectOutDTO;
import edu.bbte.beavolunteerbackend.controller.mapper.ProjectMapper;
import edu.bbte.beavolunteerbackend.model.Project;
import edu.bbte.beavolunteerbackend.repository.ProjectRepository;
import edu.bbte.beavolunteerbackend.validator.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

@Service
public class ProjectService  extends ImgService  {

    @Autowired
    private ProjectRepository projectRepository;

    public void saveProject(ProjectInDTO projectDTO, Blob img) throws BusinessException {
        if (projectRepository.checkName(projectDTO.getProject_name()) == 0) {
            Project project = ProjectMapper.projectDTOToEntity(projectDTO);
            project.setProjectImg(img);
//            set depending to token
            project.setOwnerId(4L);
            projectRepository.insertProject(project.getCreationDate(), project.getExpirationDate(),
                    project.getProjectDescription(), project.getProjectImg(), project.getProjectName(), project.getOwnerId());
        } else {
            throw new BusinessException("Project name must be unique");
        }
    }

    public void delete(Long id) {
        projectRepository.delete(projectRepository.getById(id));
    }

    public List<ProjectOutDTO> getAll() {
        return ProjectMapper.projectsToDTO(projectRepository.findAll());
    }

    public ProjectOutDTO getProjectById(Long id) {
        return ProjectMapper.projectToDTO(projectRepository.getById(id));
    }

    public ProjectOutDTO getProjectByName(String name) {
        return ProjectMapper.projectToDTO(projectRepository.getByName(name));
    }

    public byte[] getImage(Long id) throws SQLException {
        Blob projectImg = projectRepository.getById(id).getProjectImg();
        return getImg(projectImg);
    }
}
