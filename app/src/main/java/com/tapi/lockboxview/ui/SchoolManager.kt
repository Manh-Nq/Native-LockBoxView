package com.tapi.lockboxview.ui

class SchoolManager {
    private var allClass: MutableList<Class> = mutableListOf()

    fun addClass(clazz: Class) {
        allClass.add(clazz)
    }

    fun getAllClass(): List<Class> {
        return allClass
    }

    fun addStudent(classId: Int, student: Student) {
        val mClass = allClass.find { it.classID == classId }
        mClass?.addStudent(student)

    }

    fun Class.addStudent(student: Student) {
        if (!students.contains(student)) {
            students.add(student)
        }
    }

    fun Class.deleteStudentById(id: String) {
        val existed = students.find { it.id == id }
        if (existed != null) {
            students.remove(existed)
        }
    }
}