package es.KioskTV.serviceImpl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import es.KioskTV.Mapper.Mapper;
import es.KioskTV.Repository.LogRepository;
import es.KioskTV.Repository.NewsRepository;
import es.KioskTV.Repository.UserRepository;
import es.KioskTV.entity.Log;
import es.KioskTV.entity.News;
import es.KioskTV.entity.User;
import es.KioskTV.entityDTO.LogDTO;
import es.KioskTV.service.LogService;
import lombok.RequiredArgsConstructor;

/**
* Implementation of the LogService interface
*/
@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {
    @Autowired
    private final NewsRepository newRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final LogRepository logRepository;
    
    private final Mapper mapper;

    /**
     * Log creation method
     *
     * @param logDTO DTO of the log to create
     * @return The DTO of the created log
     */
    @Override
    public LogDTO createLog(LogDTO logDTO) {
        Optional<User> userOptional=userRepository.findById(logDTO.getUserDTO().getId());
        Optional<News> newsOptional=null;
        if (logDTO.getNewDTO()!=null) {
            newsOptional=newRepository.findById(logDTO.getNewDTO().getId());
        }

        Log logCreate=new Log();

		Date date=new Date();
        Timestamp timestamp=new Timestamp(date.getTime()); 
        	
        logCreate.setAction(logDTO.getAction());
        logCreate.setNews(newsOptional.get());
        logCreate.setUser(userOptional.get());
        logCreate.setCreated_at(timestamp);
        logCreate.setUpdated_at(null);
        logCreate.setDeleted_at(null);

        logRepository.save(logCreate);
        	
        return mapper.mapLogToDTO(logCreate);
    }

    /**
     * Method that returns all the logs from the DB
     *
     * @return list of logs DTOs
     */
    @Override
    public List<LogDTO> getAllLog() {
		List<LogDTO> listLogDTO=new ArrayList<>();
		List<Log> listLog=logRepository.findAll();

        if (!listLog.isEmpty()) {
            for (Log log : listLog) {
                listLogDTO.add(mapper.mapLogToDTO(log));
            }    
        }
		
		return listLogDTO;
    }

    
    /**
     * Method that obtains a log by its ID 
     * 
     * @param idLog Log ID of the log to search 
     * @return DTO of the log with specific ID 
     */
    @Override
    public Optional<LogDTO> getLogById(Long idLog) {
		Optional<Log> logOptional=logRepository.findById(idLog);

		Log log=logOptional.get();

		return Optional.of(mapper.mapLogToDTO(log));  
    }

    /**
     * method that updates the data of the log table
     *
     * @param idLog ID of the log to update
     * @param updateLog DTO with the data to be updated
     * @return DTO with updated log
     */
    @Override
    public LogDTO updateLog(Long idLog, LogDTO updateLog) {
        Optional<User> userOptional=userRepository.findById(updateLog.getUserDTO().getId());
        Optional<News> newsOptional=newRepository.findById(updateLog.getNewDTO().getId());

		Optional<Log> logOptional=logRepository.findById(idLog);

		Log log=logOptional.get();

        Date date=new Date();
    	Timestamp timestamp=new Timestamp(date.getTime()); 
        	
        log.setAction(updateLog.getAction());
    	log.setNews(newsOptional.get());
    	log.setUser(userOptional.get());
        log.setUpdated_at(timestamp);

    	logRepository.save(log);
        	
    	return mapper.mapLogToDTO(log);
    }

    /**
     * method that performs a logical deletion of a log item from the DB
     *
     * @param idLog ID of the log to delete
     */
    @Override
    public void deactivateLog(Long idLog) {
        Optional<Log> logOptional=logRepository.findById(idLog);

		Log log=logOptional.get();

        Date date=new Date();
        Timestamp timestamp=new Timestamp(date.getTime()); 
        	
        log.setDeleted_at(timestamp);

        logRepository.save(log);
    }
}