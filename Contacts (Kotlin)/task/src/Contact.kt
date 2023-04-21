package contacts
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.datetime.*
import kotlin.system.exitProcess

// enum classes required for Moshi's JSON parsing of polymorphic classes
enum class ContactType {
    PERSON,
    ORGANIZATION
}
// Contact interface set to generate an adapter for Moshi
@JsonClass(generateAdapter = true)
interface Contact {
    // Common variables with snake case names for JSON parsing
    val type : ContactType
    @Json(name="phone_number")var phoneNumber: String
    @Json(name="time_created")val timeCreated: String
    @Json(name="last_edited_time")var lastEditedTime: String

    // Common class functions
    // - Phone number
    fun setPhoneNumber()  {
            println("Enter the number:")
            phoneNumber = readln().trim()
            if(!checkPhoneNumber(phoneNumber)) {
                phoneNumber = "[no number]"
                println("Wrong number format!")
            }
    }
    // - Check number against regex for validation
    fun checkPhoneNumber(number: String): Boolean {
        val phoneCheckRegex = "^\\+?((\\(\\w+\\)[- ]?(\\w{2,})?)|(\\w+[- ]?(\\(\\w{2,}\\))?)|(\\w+[- ]?(\\w{2,})?))([- ]?(\\w{2,}))*".toRegex()
        return phoneCheckRegex.matches(number)
    }
    // - Last edited Date Time
    fun setLastEditedTime() {
        lastEditedTime = Clock.System.now().toLocalDateTime(TimeZone.of("UTC+2")).toString()
    }
    // - To set properties of each contact class
    fun setProperty()
    // - To generate a string for displaying in list/search results
    fun nameForListing(): String
}

// Person contact class set to generate an adapter for Moshi
@JsonClass(generateAdapter = true)
class Person(
             override var phoneNumber: String = String(),
             override val timeCreated: String = Clock.System.now().toLocalDateTime(TimeZone.of("UTC+2")).toString(),
             override var lastEditedTime: String = Clock.System.now().toLocalDateTime(TimeZone.of("UTC+2")).toString(),
             private var name: String = String(),
             private var surname: String = String(),
             private var birthdate: String = String(),
             private var gender: String = String()) : Contact {
    // Moshi enum type declared outside initial constructor to avoid double generation of the type when parsing to JSON
    override val type: ContactType = ContactType.PERSON
    // Add new contact details when creating the new contact
    fun addNewContactDetails() {
        setName()
        setSurname()
        setBirthDate()
        setGender()
        setPhoneNumber()
        setLastEditedTime()
        println("The record added.")
        println()
    }
    // Set property function for editing contact details
    override fun setProperty() {
        println("Select a field (name, surname, birth, gender, number):")
        when (readln().trim()) {
            "name" -> setName()
            "surname" -> setSurname()
            "birth" -> setBirthDate()
            "gender" -> setGender()
            "number" -> setPhoneNumber()
            "exit" -> exitProcess(0)
            else -> println("Invalid input!")
        }
        println("Saved")
        setLastEditedTime()
        println(this)
    }
    // Setters for the set property function
    private fun setName() {
        println("Enter the name:")
        name = readln().trim().replaceFirstChar { it.uppercase() }
   }
    private fun setSurname() {
        println("Enter the surname:")
        surname = readln().trim().replaceFirstChar { it.uppercase() }
    }
    private fun setBirthDate() {
        println("Enter birth date:")
        try {
            birthdate = LocalDate.parse(readln().trim()).toString()
        } catch(e: Exception) {
            birthdate = "[no data]"
            println("Bad birth date!")
        }
    }
    private fun setGender() {
        println("Enter the gender")
        when (readln().trim().uppercase()) {
            "M" -> gender = "M"
            "F" -> gender = "F"
            else -> {
                gender = "[no data]"
                println("Bad gender!")
            }
        }
    }
    // Name string for using lists/search results
    override fun nameForListing(): String{
        return "$name $surname"
    }
    // Override the toString function for displaying for contact details
    override fun toString(): String {
        return "Name: $name\n" +
                "Surname: $surname\n" +
                "Birth date: $birthdate\n" +
                "Gender: $gender\n" +
                "Number: $phoneNumber\n" +
                "Time created: $timeCreated\n" +
                "Time last edit: $lastEditedTime"
    }
}

// Organization contact class set to generate adaptor for Moshi
@JsonClass(generateAdapter = true)
class Organization(
                   override var phoneNumber: String = String(),
                   override val timeCreated: String = Clock.System.now().toLocalDateTime(TimeZone.of("UTC+2")).toString(),
                   override var lastEditedTime: String = Clock.System.now().toLocalDateTime(TimeZone.of("UTC+2")).toString(),
                   private var name: String = String(),
                   private var address: String = String()): Contact {
    // Moshi enum type declared outside initial constructor to avoid double generation of the type when parsing to JSON
    override val type: ContactType = ContactType.ORGANIZATION
    // Add new contact details when creating the new contact
    fun addNewContactDetails() {
        setName()
        setAddress()
        setPhoneNumber()
        setLastEditedTime()
        println("The record added.")
        println()
    }
    // Set property function for editing contact details
    override fun setProperty() {
        println("Select a field (name, address, number):")
        when (readln().trim()) {
            "name" -> setName()
            "address" -> setAddress()
            "number" -> setPhoneNumber()
            "exit" -> exitProcess(0)
            else -> println("Invalid input!")
        }
        println("Saved")
        setLastEditedTime()
        println(this)
        println()
    }
    // Setters for set property function
    private fun setName() {
        println("Enter the organization name:")
        name = readln().trim()
    }
    private fun setAddress() {
        println("Enter the address:")
        address = readln().trim()
    }
    // Name for list/search results
    override fun nameForListing(): String {
        return name
    }
    // Override toString function for displaying full contact details
    override fun toString(): String {
        return "Organization name: $name\n" +
                "Address: $address\n" +
                "Number: $phoneNumber\n" +
                "Time created: $timeCreated\n" +
                "Time last edit: $lastEditedTime"
    }
}