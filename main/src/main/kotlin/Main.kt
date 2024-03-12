package org.example

import config.XmlConfigParser

fun main() {
    val xmlParser = XmlConfigParser()
    xmlParser.parse("/config.json")
}