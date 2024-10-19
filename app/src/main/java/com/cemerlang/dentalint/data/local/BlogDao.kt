package com.cemerlang.dentalint.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.cemerlang.dentalint.data.model.blog.BlogData

@Dao
interface BlogDao {

    @Query("SELECT * FROM blogs ORDER BY created_at DESC")
    suspend fun getBlogs() : List<BlogData>

    @Query("SELECT * FROM blogs WHERE id = :blogId")
    suspend fun getBlog(blogId: String): BlogData

    @Query("SELECT * FROM blogs ORDER BY created_at DESC LIMIT 3")
    suspend fun getNewestBlogs() : List<BlogData>

    @Query("DELETE FROM blogs")
    suspend fun deleteBlogs()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBlogs(blogs: List<BlogData>)

    @Transaction
    suspend fun replaceBlogs(blogs: List<BlogData>) {
        deleteBlogs()
        insertBlogs(blogs)
    }
}