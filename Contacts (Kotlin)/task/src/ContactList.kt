package contacts
import com.squareup.moshi.*
import kotlin.system.exitProcess
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory

import java.io.File

// Program Class
class ContactList(private val contactsDataFileName: String = "contact_list.json") {
    // list to store contacts from program use
    private val contactList = mutableListOf<Contact>()
    // Moshi's variables for parsing to/from JSON for database file
    private val moshi = Moshi.Builder().add(
        // Moshi adapter for handling polymorphic classes
        PolymorphicJsonAdapterFactory.of(
            Contact::class.java, "type"
        ).withSubtype(
            Person::class.java, ContactType.PERSON.name
        ).withSubtype(
            Organization::class.java, ContactType.ORGANIZATION.name
        )).add(
            KotlinJsonAdapterFactory()
        ).build()
    private val type = Types.newParameterizedType(List::class.java, Contact::class.java)
    private val contactListAdapter = moshi.adapter<List<Contact>>(type)
    // Database file
    private val contactsDataFile = File(contactsDataFileName)

    // Read database on initialisation
    init {
        readDataFile()
    }
    // Read database file function
    private fun readDataFile() {
        contactsDataFile.createNewFile()
        val fileContent = contactsDataFile.readText()
        if (fileContent.isNotEmpty()) {
            val list = contactListAdapter.fromJson(fileContent)
            list!!.forEach { contactList.add(it) }
        }
        println("open $contactsDataFileName")
        println()
    }
    // For writing to database file
    private fun writeDataFile() {
        contactsDataFile.writeText(contactListAdapter.toJson(contactList))
    }
    // Main menu function
    fun mainMenu() {
        while(true) {
            println("[menu] Enter action (add, list, search, count, exit):")
            when (readln()) {
                "add" -> add()
                "list" -> list()
                "search" -> search()
                "count" -> count()
                "exit" -> exit()
                else -> println("Invalid input!")
            }
        }
    }
    // Exit function
    private fun exit() {
        exitProcess(0)
    }
    // Add contact function
    private fun add() {
        println("Enter the type (person, organization):")
        when (readln().trim()) {
            "person" -> {
                val newPerson = Person()
                newPerson.addNewContactDetails()
                contactList.add(newPerson)
                writeDataFile() // update database file after creating new contact
            }
            "organization" -> {
                val newOrganization = Organization()
                newOrganization.addNewContactDetails()
                contactList.add(newOrganization)
                writeDataFile() // update database file after creating new contact
            }
            else -> println("Invalid Input!")
        }
    }
    // Search contacts function
    private fun search() {
        println("Enter a search query:")
        val query = readln().trim()
        val resultsList = mutableListOf<Contact>()
        contactList.forEach {if (it.toString().lowercase().contains(query)) resultsList.add(it) }
        if (resultsList.size == 0) {
            println("No results found.")
            println()
        } else {
            for(contact in resultsList) {
                println("${resultsList.indexOf(contact) + 1}. ${contact.nameForListing()}")
            }
            println()
            // Search results menu
            searchMenu()
        }
    }
    // Search results menu function
    private fun searchMenu() {
        println("[search] Enter action ([number], back, again):")
        val action = readln().trim()
        when {
            // for selecting a listed result by number
            action.matches("^\\d*$".toRegex()) -> {
                if (action.toInt() <= contactList.size) {
                    contactMenu(contactList[action.toInt() -1])
                } else {
                    println("Invalid input!")
                }
            }
            action == "back" -> return
            action == "again" -> return search()
        }
    }
    // Contact menu function
    private fun contactMenu(contact: Contact) {
        println("[record] Enter action (edit, delete, menu):")
        when (readln().trim()) {
            "edit" -> {
                contact.setProperty()
                writeDataFile()
            }
            "delete" -> {
                delete(contact)
                writeDataFile()
            }
            "menu" -> return
            "else" -> return println("Invalid input!")
        }
    }
    // Delete contact function
    private fun delete(contact: Contact) {
        contactList.remove(contact)
        println("The record removed!")
        println()
    }
    // Count contacts function
    private fun count() {
        println("The Phone Book has ${contactList.size} records")
        println()
    }
    // List contacts function
    private fun list() {
        if (contactList.isEmpty()) {
            count()
        } else {
            for (contact in contactList) {
                println("${contactList.indexOf(contact) + 1}. ${contact.nameForListing()}")
            }
            println()
            listMenu()
        }
    }
    // List menu function
    private fun listMenu() {
        println("[list] Enter action ([number], back):")
        val action = readln().trim()
        when {
            // for selecting a listed result by number
            action.matches("^\\d*$".toRegex()) -> {
                if (action.toInt() <= contactList.size) {
                    println(contactList[action.toInt() -1])
                    println()
                    contactMenu(contactList[action.toInt() -1])
                } else {
                    println("Invalid input!")
                }
            }
            action == "back" -> return
            else -> println("Invalid input!")
        }
        println()
    }
}