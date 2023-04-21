package contacts

fun main(args: Array<String>) {
    // Search start program with or without a supplied database in the command line parameters
    if (args.isNotEmpty()) {
        val contactList = ContactList(args[0])
        contactList.mainMenu()
    } else {
        val contactList = ContactList()
        contactList.mainMenu()
    }

}

