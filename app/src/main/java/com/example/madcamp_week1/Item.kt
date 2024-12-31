package com.example.madcamp_week1


sealed class Item {
    data class TypeA(val title: String, val text: String, val date: String) : Item()
    data class TypeB(val imageRes: List<Long>) : Item()
    data class TypeC(val location: String) : Item()
    data class Item1(val text: String)
}