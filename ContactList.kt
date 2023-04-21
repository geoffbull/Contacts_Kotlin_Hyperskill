class ContactList {

    // Get new contact information
    fun getNewContactInformation(): Contact {
        println("Enter the name of the person")
        val name = readln()

        return Contact(name, surname, phoneNumber)
    }
}