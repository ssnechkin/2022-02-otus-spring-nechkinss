package ru.otus.homework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.otus.homework.domain.mongo.Author;

import java.util.List;

@Service
public class AuthorBatchService {
    private final Logger logger = LoggerFactory.getLogger("AuthorBatch");

    public ItemReadListener<Author> getItemReadListener() {
        return new ItemReadListener<>() {
            public void beforeRead() {
                logger.info("Начало чтения");
            }

            public void afterRead(@NonNull Author o) {
                logger.info("Конец чтения");
            }

            public void onReadError(@NonNull Exception e) {
                logger.info("Ошибка чтения");
            }
        };
    }

    public JobExecutionListener getJobExecutionListener() {
        return new JobExecutionListener() {
            @Override
            public void beforeJob(@NonNull JobExecution jobExecution) {
                logger.info("Начало job");
            }

            @Override
            public void afterJob(@NonNull JobExecution jobExecution) {
                logger.info("Конец job");
            }
        };
    }

    public ItemWriteListener<Author> getItemWriteListener() {
        return new ItemWriteListener<>() {
            public void beforeWrite(@NonNull List list) {
                logger.info("Начало записи");
            }

            public void afterWrite(@NonNull List list) {
                logger.info("Конец записи");
            }

            public void onWriteError(@NonNull Exception e, @NonNull List list) {
                logger.info("Ошибка записи");
            }
        };
    }

    public ItemProcessListener<Author, Author> getItemProcessListener() {
        return new ItemProcessListener<>() {
            public void beforeProcess(Author o) {
                logger.info("Начало обработки");
            }

            public void afterProcess(@NonNull Author o, Author o2) {
                logger.info("Конец обработки");
            }

            public void onProcessError(@NonNull Author o, @NonNull Exception e) {
                logger.info("Ошибка обработки");
            }
        };
    }

    public ChunkListener getChunkListener() {
        return new ChunkListener() {
            public void beforeChunk(@NonNull ChunkContext chunkContext) {
                logger.info("Начало пачки");
            }

            public void afterChunk(@NonNull ChunkContext chunkContext) {
                logger.info("Конец пачки");
            }

            public void afterChunkError(@NonNull ChunkContext chunkContext) {
                logger.info("Ошибка пачки");
            }
        };
    }

}
