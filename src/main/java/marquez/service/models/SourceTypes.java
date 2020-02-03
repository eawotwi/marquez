/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package marquez.service.models;

import static marquez.common.base.MorePreconditions.checkNotBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;
import marquez.common.models.SourceQualifier;

@EqualsAndHashCode
@ToString
public final class SourceTypes {
  @Value
  static final class Db {
    String type;
    List<String> dataTypes;

    @JsonCreator
    public Db(final String type, @NonNull final List<String> dataTypes) {
      this.type = checkNotBlank(type, "type must not be blank").toUpperCase();
      this.dataTypes = dataTypes;
    }

    boolean supportsDataType(@NonNull String dataType) {
      return false;
    }
  }

  @Value
  static final class Stream {
    String type;

    @JsonCreator
    public Stream(final String type) {
      this.type = checkNotBlank(type, "type must not be blank").toUpperCase();
    }
  }

  @Value
  static final class Filesystem {
    String type;

    @JsonCreator
    public Filesystem(final String type) {
      this.type = checkNotBlank(type, "type must not be blank");
    }
  }

  @Setter private List<Db> dbs;
  @Setter private List<Stream> streams;
  @Setter private List<Filesystem> filesystems;

  private Map<String, Db> typeToDbMap = null;

  @JsonCreator
  public SourceTypes(
      @Nullable final List<Db> dbs,
      @Nullable final List<Stream> streams,
      @Nullable final List<Filesystem> filesystems) {
    this.dbs = ImmutableList.copyOf(new ArrayList<>(dbs));
    this.streams = ImmutableList.copyOf(new ArrayList<>(streams));
    this.filesystems = ImmutableList.copyOf(new ArrayList<>(filesystems));
  }

  public boolean exists(String type) {
    return false;
  }

  public boolean dbSupportsDataType(@NonNull String type, @NonNull String dataType) {
    return false;
  }

  private static final Db MYSQL =
      new Db(
          "MySQL",
          ImmutableList.of(
              "INTEGER",
              "INT",
              "SMALLINT",
              "TINYINT",
              "MEDIUMINT",
              "BIGINT",
              "DECIMAL",
              "NUMERIC",
              "FLOAT",
              "REAL",
              "DOUBLE",
              "DOUBLE PRECISION",
              "BIT",
              "DATE",
              "DATETIME",
              "TIMESTAMP",
              "TIME",
              "YEAR",
              "CHAR",
              "VARCHAR",
              "BINARY",
              "VARBINARY",
              "TINYBLOB",
              "BLOB",
              "MEDIUMBLOB",
              "LONGBLOB",
              "TINYTEXT",
              "TEXT",
              "MEDIUMTEXT",
              "LONGTEXT",
              "ENUM",
              "SET",
              "GEOMETRY",
              "POINT",
              "LINESTRING",
              "POLYGON",
              "MULTIPOINT",
              "MULTILINESTRING",
              "MULTIPOLYGON",
              "GEOMETRYCOLLECTION",
              "JSON"));
  private static final Db POSTGRESQL = new Db("PostgreSQL", ImmutableList.of(""));
  private static final Db REDSHIFT = new Db("Redshift", ImmutableList.of(""));
  private static final Db SNOWFLAKE = new Db("Snowflake", ImmutableList.of(""));
  private static final List<Db> DBS = ImmutableList.of(MYSQL, POSTGRESQL, REDSHIFT, SNOWFLAKE);

  private static final Stream KAFKA = new Stream("Kafka");
  private static final Stream KINESIS = new Stream("Kinesis");
  private static final List<Stream> STREAMS = ImmutableList.of(KAFKA, KINESIS);

  private static final Filesystem S3 = new Filesystem("S3");
  private static final Filesystem GCS = new Filesystem("GCS");
  private static final List<Filesystem> FILESYSTEMS = ImmutableList.of(S3, GCS);

  public static final SourceTypes ALL = new SourceTypes(DBS, STREAMS, FILESYSTEMS);
}
