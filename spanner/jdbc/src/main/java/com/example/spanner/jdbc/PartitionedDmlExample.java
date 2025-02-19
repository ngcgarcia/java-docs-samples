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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

class PartitionedDmlExample {

  static void partitionedDml() throws SQLException {
    // TODO(developer): Replace these variables before running the sample.
    String projectId = "test-project";
    String instanceId = "test-instance";
    String databaseId = "my-database";
    partitionedDml(projectId, instanceId, databaseId);
  }

  @SuppressFBWarnings(
      value = "OBL_UNSATISFIED_OBLIGATION",
      justification = "https://github.com/spotbugs/spotbugs/issues/293")
  static void partitionedDml(String projectId, String instanceId, String databaseId)
      throws SQLException {
    String connectionUrl =
        String.format(
            "jdbc:cloudspanner://localhost:9010/projects/%s/instances/%s/databases/%s;usePlainText=true",
            projectId, instanceId, databaseId);
    try (Connection connection = DriverManager.getConnection(connectionUrl);
        Statement statement = connection.createStatement()) {
      // Turn on partitioned DML mode.
      statement.execute("SET AUTOCOMMIT_DML_MODE = 'PARTITIONED_NON_ATOMIC'");
      int updateCount =
          statement.executeUpdate(
              "UPDATE Singers SET SingerInfo = SHA512(LastName)\n"
                  + "WHERE SingerId >= 1 AND SingerId <= 5");
      System.out.printf("Updated %d row(s)%n", updateCount);
    }
  }
}
