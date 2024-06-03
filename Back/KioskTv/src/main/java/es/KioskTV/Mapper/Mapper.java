package es.KioskTV.Mapper;

import es.KioskTV.entity.Log;
import es.KioskTV.entity.News;
import es.KioskTV.entity.User;
import es.KioskTV.entityDTO.LogDTO;
import es.KioskTV.entityDTO.NewDTO;
import es.KioskTV.entityDTO.UserDTO;

/**
 * Class that maps entities to DTO
 */
public class Mapper {

    /**
     * Method that maps a user to a userDTO
     *
     * @param user user that maps to userDTO
     * @return userDTO with the user data
     */
    public UserDTO mapUserToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setDas(user.getDas());
        userDTO.setLastName(user.getLastName());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    /**
     * Method that maps a news to a newDTO
     *
     * @param news news that maps to newsDTO
     * @return newsDTO with the news data
     */
    public NewDTO mapNewToDTO(News news) {
        NewDTO newsDTO = new NewDTO();
        newsDTO.setId(news.getId());
        newsDTO.setTitle(news.getTitle());
        newsDTO.setDescription(news.getDescription());
        newsDTO.setExpireDate_at(news.getExpireDate_at());
        newsDTO.setFilePath(news.getFilePath());
        newsDTO.setUserDTO(mapUserToDTO(news.getUser()));
        return newsDTO;
    }

    /**
     * Method that maps a log to a logDTO
     *
     * @param log log that maps to logDTO
     * @return logDTO with the log data
     */
    public LogDTO mapLogToDTO(Log log) {
        LogDTO logDTO = new LogDTO();
        logDTO.setId(log.getId());
        logDTO.setAction(log.getAction());
        logDTO.setNewDTO(mapNewToDTO(log.getNews()));
        logDTO.setUserDTO(mapUserToDTO(log.getUser()));
        logDTO.setCreated_at(log.getCreated_at());
        return logDTO;
    }
}