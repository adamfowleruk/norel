/*! ******************************************************************************
 *
 * NoREL API
 *
 * Copyright (C) 2018 by Adam Fowler : http://www.adamfowler.org/
 *
 *******************************************************************************
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ******************************************************************************/
package org.norelapi.impl.shared;

import org.norelapi.core.OperationException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner;
import java.io.BufferedReader;

public class IOUtils {
  public static String readInputStreamIntoString(InputStream docSrc) throws OperationException {
    StringBuilder fileContents = new StringBuilder();
    Scanner scanner = new Scanner(((Readable)new BufferedReader(new InputStreamReader(docSrc)) )); // ensures entire stream read
    String lineSeparator = System.getProperty("line.separator");

    try {
      while(scanner.hasNextLine()) {
        fileContents.append(scanner.nextLine());
        if (scanner.hasNextLine()) {
          fileContents.append(lineSeparator);
        }
      }
    } finally {
      scanner.close();
    }
    return fileContents.toString();
  }
}