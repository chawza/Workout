package com.nabeelkm.workout.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

enum class ParameterType(val value: Int) {
    STRING(1),
    INTEGER(2),
    FLOAT(3);

    override fun toString(): String {
        return when(this) {
            STRING -> "String"
            FLOAT -> "Float"
            INTEGER -> "Integer"
        }
    }

    companion object {
        fun fromValue(value: Int): ParameterType {
            return ParameterType.entries.find { it.value == value }!!
        }
    }

}

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Goal::class,
            parentColumns = ["id"],
            childColumns = ["goalId"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [
        Index(value = ["goalId"])
    ]
)
data class Parameter(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val goalId: Int,
    val name: String,
    val type: Int,
    val unit: String? = null
) {
    fun getType(): ParameterType {
        return ParameterType.entries[type]
    }
}