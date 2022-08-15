package cn.wearbbs.note.database.dao

import androidx.room.*
import cn.wearbbs.note.database.bean.Note

@Dao
interface NoteDao {
    @Query("SELECT * FROM note")
    suspend fun getAll(): MutableList<Note>

    @Query("SELECT * FROM note WHERE id LIKE :id")
    suspend fun findById(id: Int): Note

    @Query("SELECT * FROM note WHERE name LIKE :name LIMIT 1")
    suspend fun findByName(name: String): Note

    @Insert
    suspend fun insertAll(vararg notes: Note)

    @Delete
    suspend fun delete(note: Note)

    @Update
    suspend fun update(note: Note)
}