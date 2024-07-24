package com.multi.toonGather.config;


import com.multi.toonGather.recruit.model.dto.creator.CreatorDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = "com.multi.toonGather", annotationClass = Mapper.class)
public class MybatisConfig {
    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public SqlSessionFactory sqlSessoinFactory(DataSource datasource) throws Exception{
        SqlSessionFactoryBean seb  = new SqlSessionFactoryBean();
        Resource[] res = new PathMatchingResourcePatternResolver().getResources("classpath:mappers/**/*.xml");

        seb.setMapperLocations(res);

        seb.setDataSource(datasource);

        org.apache.ibatis.session.Configuration configuration = new  org.apache.ibatis.session.Configuration();
        configuration.setJdbcTypeForNull(JdbcType.NULL);

        // Type Aliases 설정
        configuration.getTypeAliasRegistry().registerAlias("jobDTO", com.multi.toonGather.recruit.model.dto.job.JobDTO.class);
        configuration.getTypeAliasRegistry().registerAlias("pageDTO", com.multi.toonGather.common.model.dto.PageDTO.class);
        configuration.getTypeAliasRegistry().registerAlias("creatorDTO", CreatorDTO.class);
        configuration.getTypeAliasRegistry().registerAlias("applyDTO", com.multi.toonGather.recruit.model.dto.job.ApplyDTO.class);
        // 희철 cs
        configuration.getTypeAliasRegistry().registerAlias("QuestionDTO", com.multi.toonGather.cs.model.dto.QuestionDTO.class);
        configuration.getTypeAliasRegistry().registerAlias("CsCategoryDTO", com.multi.toonGather.cs.model.dto.CsCategoryDTO.class);
        configuration.getTypeAliasRegistry().registerAlias("CsStatusDTO", com.multi.toonGather.cs.model.dto.CsStatusDTO.class);
        // 서윤 social
        configuration.getTypeAliasRegistry().registerAlias("reviewDTO", com.multi.toonGather.social.model.dto.ReviewDTO.class);
        configuration.getTypeAliasRegistry().registerAlias("writerDTO", com.multi.toonGather.social.model.dto.WriterDTO.class);

        seb.setConfiguration(configuration);

        return seb.getObject();


    }


}
