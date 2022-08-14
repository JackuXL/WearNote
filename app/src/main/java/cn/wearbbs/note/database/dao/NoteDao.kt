package cn.wearbbs.note.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import cn.wearbbs.note.database.bean.Note

@Dao
interface NoteDao {
    @Query("SELECT * FROM note")
    fun getAll(): List<Note>

    @Query("SELECT * FROM note WHERE id LIKE :id")
    fun findById(id:Int): Note

    @Query("SELECT * FROM note WHERE name LIKE :name LIMIT 1")
    fun findByName(name:String): Note

    @Insert
    fun insertAll(vararg notes: Note)

    @Delete
    fun delete(note: Note)

    @Update
    fun update(note: Note)
}