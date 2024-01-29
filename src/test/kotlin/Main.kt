import data.TestData
import me.outspending.core.storage.database.DatabaseHandler
import me.outspending.core.storage.database.serializer.SerializerManager

val sqlUpdate = "UPDATE test_data SET name = ? WHERE id = ?;"

fun main() {

    val data = TestData("id", "test")
    val mockStatement = DatabaseHandler.databaseConnection.prepareStatement(sqlUpdate)
    val serialized = SerializerManager(data).serialize(mockStatement)

    println(serialized)
}