/**
 * Copyright 2016 Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 * 
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
package com.salsaw.msalsa.services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Generic class that permit to serialize and deserialize object using standard Java object serialization
 * 
 * @author Alessandro Daniele, Fabio Cesarato, Andrea Giraldin
 *
 */
public class ObjectSerializer<T> {

	private final String filePath;

	public ObjectSerializer(String filePath) {
		if (filePath == null || filePath.isEmpty() == true) {
			throw new IllegalArgumentException("salsaParameters");
		}

		this.filePath = filePath;
	}

	/**
	 * Read a Java object stored inside file system
	 * 
	 * @param entity
	 * @throws IOException
	 */
	public void serialize(T entity) throws IOException {
		try (FileOutputStream fileOut = new FileOutputStream(this.filePath)) {
			try (ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
				out.writeObject(entity);
			}
		}
	}

	/**
	 * Write a Java object inside file system
	 * 
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public T deserialize() throws FileNotFoundException, IOException, ClassNotFoundException {
		try (FileInputStream fileIn = new FileInputStream(this.filePath)) {
			try (ObjectInputStream in = new ObjectInputStream(fileIn)) {
				return (T) in.readObject();
			}
		}
	}
}
