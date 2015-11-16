package ua.co.ur6lad.stroke;

/*
 * Copyright 2015 Vitaliy Berdinskikh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * Use when a variable is unsupported.
 *
 * @author Vitaliy Berdinskikh
 */
public class UnsupportedVariableException extends Exception {

	private static final long serialVersionUID = -7831469260923268532L;

	public UnsupportedVariableException() {}

	public UnsupportedVariableException(String message) {
		super(message);
	}

	public UnsupportedVariableException(Throwable cause) {
		super(cause);
	}

	public UnsupportedVariableException(String message, Throwable cause) {
		super(message, cause);
	}

}