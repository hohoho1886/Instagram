package org.ninh.instaclone.repository

import org.springframework.core.io.ResourceLoader
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.nio.charset.StandardCharsets
import org.springframework.beans.factory.annotation.Value

@Repository
class FollowRepository(
    private val jdbcTemplate: JdbcTemplate,
    private val resourceLoader: ResourceLoader,
    @param:Value("\${sql.path}")
    private val sqlPath: String
) {
    fun findFollowees(followerUsername: String): List<String>{
        val resource = resourceLoader.getResource(sqlPath)
        val sql = resource.inputStream.bufferedReader(StandardCharsets.UTF_8).use { it.readText() }
        return jdbcTemplate.queryForList(sql, String::class.java, followerUsername) as List<String>
    }
}