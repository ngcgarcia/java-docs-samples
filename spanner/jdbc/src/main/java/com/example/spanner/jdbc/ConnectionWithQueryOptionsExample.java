/*
 * Copyright 2020 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.spanner.jdbc;

//[START spanner_jdbc_connection_with_query_options]
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class ConnectionWithQueryOptionsExample {

  static void connectionWithQueryOptions() throws SQLException {
    // TODO(developer): Replace these variables before running the sample.
    String projectId = "test-project";
    String instanceId = "test-instance";
    String databaseId = "my-database";
    connectionWithQueryOptions(projectId, instanceId, databaseId);
  }

  @SuppressFBWarnings(
      value = "OBL_UNSATISFIED_OBLIGATION",
      justification = "https://github.com/spotbugs/spotbugs/issues/293")
  static void connectionWithQueryOptions(String projectId, String instanceId, String databaseId)
      throws SQLException {
    String optimizerVersion = "1";
    String connectionUrl =
        String.format(
            "jdbc:cloudspanner://localhost:9010/projects/%s/instances/%s/databases/%s?optimizerVersion=%s;usePlainText=true",
            projectId, instanceId, databaseId, optimizerVersion);
    try (Connection connection = DriverManager.getConnection(connectionUrl);
        Statement statement = connection.createStatement()) {
      // Execute a query using the optimizer version '1'.
      try (ResultSet rs =
          statement.executeQuery(
              "SELECT SingerId, FirstName, LastName FROM Singers ORDER BY LastName")) {
        while (rs.next()) {
          System.out.printf("%d %s %s%n", rs.getLong(1), rs.getString(2), rs.getString(3));
        }
      }
      try (ResultSet rs = statement.executeQuery("SHOW VARIABLE OPTIMIZER_VERSION")) {
        while (rs.next()) {
          System.out.printf("Optimizer version: %s%n", rs.getString(1));
        }
      }
    }
  }
}
// [END spanner_jdbc_connection_with_query_options]
