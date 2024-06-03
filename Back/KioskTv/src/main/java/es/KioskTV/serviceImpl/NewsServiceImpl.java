package es.KioskTV.serviceImpl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import es.KioskTV.Mapper.Mapper;
import es.KioskTV.Repository.NewsRepository;
import es.KioskTV.Repository.UserRepository;
import es.KioskTV.entity.News;
import es.KioskTV.entity.User;
import es.KioskTV.entityDTO.NewDTO;
import es.KioskTV.service.NewService;
import lombok.RequiredArgsConstructor;

/**
 * Implementation of the NewService interface
 */
@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewService {
    @Autowired
    private final NewsRepository newsRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private Mapper mapper;

    /**
     * News creation method
     *
     * @param newsDTO DTO of the news to create
     * @return The DTO of the created news
     */
    @Override
    public NewDTO createNews(NewDTO newsDTO) {
        News news = new News();
        news.setTitle(newsDTO.getTitle());
        news.setDescription(newsDTO.getDescription());
        news.setFilePath(newsDTO.getFilePath());
        news.setExpireDate_at(newsDTO.getExpireDate_at());
        news.setCreated_at(new Timestamp(System.currentTimeMillis()));

        User user = userRepository.findById(newsDTO.getUserDTO().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        news.setUser(user);
        News savedNews = newsRepository.save(news);
        return mapper.mapNewToDTO(savedNews);
    }

    /**
     * Method that obtains a news by its ID
     * 
     * @param idNews News ID of the news to search
     * @return DTO of the news with specific ID
     */
    @Override
    public Optional<NewDTO> getNewsById(Long idNews) {
        Optional<News> newsOptional = newsRepository.findById(idNews);
        if (newsOptional.isPresent()) {
            News news = newsOptional.get();
            if (news.getDeleted_at() == null) {
                return Optional.of(mapper.mapNewToDTO(news));
            }
        }
        return Optional.empty();
    }

    /**
     * Method that returns all the news from the DB
     *
     * @return list of news DTOs
     */
    @Override
    public List<NewDTO> getAllNews() {
        List<NewDTO> listNewsDTO = new ArrayList<>();
        List<News> listNews = newsRepository.findAll();

        for (News news : listNews) {
            if (news.getDeleted_at() == null) {
                listNewsDTO.add(mapper.mapNewToDTO(news));
            }
        }

        return listNewsDTO;
    }

    /**
     * Method that updates the data of the news table
     *
     * @param idNews     ID of the news to update
     * @param updateNews DTO with the data to be updated
     * @return DTO with updated news
     */
    @Override
    public NewDTO updateNews(Long idNews, NewDTO updateNews) {
        Optional<News> newsOptional = newsRepository.findById(idNews);
        if (newsOptional.isPresent()) {
            News news = newsOptional.get();

            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());

            news.setTitle(updateNews.getTitle());
            news.setDescription(updateNews.getDescription());
            news.setFilePath(updateNews.getFilePath()); // Asegúrate de que esta es la URL actualizada del archivo
            news.setExpireDate_at(updateNews.getExpireDate_at());
            news.setUpdated_at(timestamp);

            newsRepository.save(news);
            return mapper.mapNewToDTO(news);
        }
        return null;
    }

    /**
     * Method that performs a logical deletion of a news item from the DB
     *
     * @param idNews ID of the news to delete
     */
    @Override
    public void deactivateNews(Long idNews) {
        Optional<News> newsOptional = newsRepository.findById(idNews);
        if (newsOptional.isPresent()) {
            News news = newsOptional.get();

            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());

            news.setDeleted_at(timestamp);

            newsRepository.save(news);
        }
    }
}