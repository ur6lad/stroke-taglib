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

import java.util.HashMap;
import java.util.Map;

/**
 * The base class of variables.
 *
 * The variable has number and prefix, its name is prefix+number. All variables are comparable.
 *
 * @author Vitaliy Berdinskikh
 */
public abstract class OmniVariable implements Comparable<OmniVariable> {

	private Integer number;

	public abstract String getPrefix();

	public OmniVariable(int number) {
		this.number = number;
	}

	@Override
	public int compareTo(OmniVariable that) {
		int result = 0;

		if (null == that) {
			result = Integer.MIN_VALUE;
		} else {
			result = getNumber().compareTo(that.getNumber());
		}

		return result;
	}

	@Override
	public boolean equals(Object that) {
		if (this.getClass().isInstance(that)) {
			OmniVariable thatVariable = (OmniVariable) that;
			return getNumber().equals(thatVariable.getNumber());
		} else {
			return false;
		}
	}

	public Integer getNumber() {
		return number;
	}

	@Override
	public int hashCode() {
		return getNumber().hashCode();
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append(getPrefix()).append(number);

		return buffer.toString();
	}

	/**
	 * Conversion variable eVarN.
	 *
	 * @author Vitaliy Berdinskikh
	 */
	public static class Conversion extends OmniVariable {

		private static final String EVAR = "eVar";

		public Conversion(int number) {
			super(number);
		}

		@Override
		public String getPrefix() {
			return EVAR;
		}

	}

	/**
	 * Event could have name. If event has name its number is {@link Integer#MIN_VALUE}.
	 * Available names are Add, Checkout, Open, ProductView, Purchase, Remove and View, see {@link OmniVariable.Event.Name}.
	 *
	 * @author Vitaliy Berdinskikh
	 */
	public static class Event extends OmniVariable {

		private static final String COLON = ":";
		private static final String EVENT = "event";

		private Name name;
		private String serial;

		public Event(int number) {
			super(number);
		}

		public Event(int number, String serial) {
			super(number);
			this.serial = prepareSerial(serial);
		}


		public Event(Name name) throws IllegalArgumentException {
			super(Integer.MIN_VALUE);
			if (null == name) {
				throw new IllegalArgumentException("Event name: null");
			}
			this.name = name;
		}

		public Event(Name name, String serial) throws IllegalArgumentException {
			this(name);
			this.serial = prepareSerial(serial);
		}

		public Event(String name) throws IllegalArgumentException {
			this(Name.forName(name));
		}

		public Event(String name, String serial) throws IllegalArgumentException {
			this(Name.forName(name), serial);
		}

		@Override
		public int compareTo(OmniVariable that) {
			int result;
			Event thatEvent;

			// If it's an event try to order them by name
			if (that instanceof Event) {
				if (null != getName()) {
					thatEvent = (Event) that;
					if (null == thatEvent.getName()) {
						// Named events go first
						result = Integer.MIN_VALUE;
					} else {
						result = getName().toString().compareTo(thatEvent.getName().toString());
					}
				} else {
					result = super.compareTo(that);
				}
				// QUESTION: should I order it by a serial?
				if (0 == result) {
					thatEvent = (Event) that;
					if (null != getSerial() && null != thatEvent.getSerial()) {
						result = getSerial().compareTo(thatEvent.getSerial());
					} else if (null != getSerial()) {
						result = Integer.MAX_VALUE;
					} else if (null != thatEvent.getSerial()) {
						result = Integer.MIN_VALUE;
					}
				}
			} else {
				result = super.compareTo(that);
			}

			return result;
		}

		@Override
		public boolean equals(Object that) {
			if (that instanceof Event) {
				Event thatEvent = (Event) that;

				if (null != getName()) {
					return getName().equals(thatEvent.getName());
				} else {
					return super.equals(that);
				}
			} else {
				return false;
			}
		}

		public Name getName() {
			return name;
		}

		@Override
		public String getPrefix() {
			return EVENT;
		}

		public String getSerial() {
			return serial;
		}

		@Override
		public int hashCode() {
			return (null == getName()) ? getNumber().hashCode() : getName().toString().hashCode() ;
		}

		@Override
		public String toString() {
			StringBuilder buffer = new StringBuilder();

			if (null != getName()) {
				buffer.append(getName());
			} else {
				buffer.append(getPrefix()).append(getNumber());
			}
			if (null != getSerial()) {
				buffer.append(COLON).append(getSerial());
			}

			return buffer.toString();
		}

		protected String prepareSerial(String str) {
			if (null != str) {
				str = str.trim();
				if (str.isEmpty()) {
					str = null;
				}
			}

			return str;
		}

		/**
		 * Event's names.
		 *
		 * @author Vitaliy Berdinskikh
		 */
		public enum Name {

			/**
			 * The scAdd event.
			 */
			Add("scAdd"),
			/**
			 * The scCheckout event.
			 */
			Checkout("scCheckout"),
			/**
			 * The scOpen event.
			 */
			Open("scOpen"),
			/**
			 * The prodView event.
			 */
			ProductView("prodView"),
			/**
			 * The purchase event.
			 */
			Purchase("purchase"),
			/**
			 * The scRemove event.
			 */
			Remove("scRemove"),
			/**
			 * The scView event.
			 */
			View("scView");

			private String name;

			public String getName() {
				return name;
			}

			public String toString() {
				return getName();
			}

			/**
			 * Case insensitive replacement of <tt>valueOf(name)</tt>.
			 *
			 * @param name variable's name in any case
			 * @return named variable
			 * @see Name#valueOf
			 * @throws IllegalArgumentException if a name is not found
			 * @throws NullPointerException if a name is null
			 */
			public static Name forName(final String name) throws IllegalArgumentException {
				Name variable;

				if (null == name) {
					throw new IllegalArgumentException("Name of variable is null");
				}

				variable = NAME_MAP.get(name.toLowerCase());
				if (null == variable) {
					throw new IllegalArgumentException("Name not found: " + name);
				}

				return variable;
			}

			private static final Map<String, Name> NAME_MAP = new HashMap<String, Name>();

			static {
				for (Name namedVariable : Name.values()) {
					NAME_MAP.put(namedVariable.getName().toLowerCase(), namedVariable);
				}
			}

			private Name(String name) {
				this.name = name;
			}

		}

	}

	/**
	 * Hierarchy variable hierN.
	 *
	 * @author Vitaliy Berdinskikh
	 */
	public static class Hierarchy extends OmniVariable {

		private static final String HIER = "hier";

		public Hierarchy(int number) {
			super(number);
		}

		@Override
		public String getPrefix() {
			return HIER;
		}

	}

	/**
	 * List variable listN.
	 *
	 * @author Vitaliy Berdinskikh
	 */
	public static class List extends OmniVariable {

		private static final String LIST = "list";

		public List(int number) {
			super(number);
		}

		@Override
		public String getPrefix() {
			return LIST;
		}

	}

	/**
	 * Standard variable has name only. its number isn't use and equals {@link Integer#MIN_VALUE}.
	 * Available names see {@link OmniVariable.Standard.Name}.
	 *
	 * @author Vitaliy Berdinskikh
	 */
	public static class Standard extends OmniVariable {

		private static final String EMPTY = "";
		private Name name;

		public Standard(Name name) throws IllegalArgumentException {
			super(Integer.MIN_VALUE);
			if (null == name) {
				throw new IllegalArgumentException("Event name: null");
			}
			this.name = name;
		}

		public Standard(String name) throws IllegalArgumentException {
			this(Name.forName(name));
		}

		@Override
		public int compareTo(OmniVariable that) {
			int result;
			Standard thatVariable;

			// If it's an event try to order them by name
			if (that instanceof Standard) {
				thatVariable = (Standard) that;
				result = getName().toString().compareTo(thatVariable.getName().toString());
			} else {
				result = super.compareTo(that);
			}

			return result;
		}

		@Override
		public boolean equals(Object that) {
			if (that instanceof Standard) {
				Standard thatVariable = (Standard) that;

				return getName().equals(thatVariable.getName());
			} else {
				return false;
			}
		}

		public Name getName() {
			return name;
		}

		@Override
		public String getPrefix() {
			return EMPTY;
		}

		@Override
		public int hashCode() {
			return getName().toString().hashCode();
		}

		@Override
		public String toString() {
			return getName().toString();
		}

		/**
		 * Standard's names.
		 *
		 * @author Vitaliy Berdinskikh
		 */
		public enum Name {

			/**
			 * The s_account variable. It's read-only: you cannot set it.
			 */
			Account("s_account", Type.ReadOnly),
			/**
			 * The browserHeight variable. It's read-only: you cannot set it.
			 */
			BrowserHeight("browserHeight", Type.ReadOnly),
			/**
			 * The browserWidth variable. It's read-only: you cannot set it.
			 */
			BrowserWidth("browserWidth", Type.ReadOnly),
			/**
			 * The campaign variable.
			 */
			Campaign("campaign"),
			/**
			 * The channel variable.
			 */
			Channel("channel"),
			/**
			 * The charSet variable.
			 */
			CharSet("charSet"),
			/**
			 * The colorDepth variable. It's read-only: you cannot set it.
			 */
			ColorDepth("colorDepth", Type.ReadOnly),
			/**
			 * The connectionType variable. It's read-only: you cannot set it.
			 */
			ConnectionType("connectionType", Type.ReadOnly),
			/**
			 * The eVar variables. Use {@link OmniVariable.Conversion} to set any eVar.
			 */
			Conversion("eVar", Type.Complex),
			/**
			 * The cookieDomainPeriods variable.
			 */
			CookieDomainPeriods("cookieDomainPeriods"),
			/**
			 * The cookieLifetime variable.
			 */
			CookieLifetime("cookieLifetime"),
			/**
			 * The cookiesEnabled variable. It's read-only: you cannot set it.
			 */
			CookiesEnabled("cookiesEnabled", Type.ReadOnly),
			/**
			 * The currencyCode variable.
			 */
			CurrencyCode("currencyCode"),
			/**
			 * The dc variable.
			 */
			DataCenter("dc"),
			/**
			 * The doPlugins variable. It's read-only: you cannot set it.
			 */
			DoPlugins("doPlugins", Type.ReadOnly),
			/**
			 * The dynamicAccountList variable.
			 */
			DynamicAccountList("dynamicAccountList"),
			/**
			 * The dynamicAccountMatch variable. The value is used as is.
			 */
			DynamicAccountMatch("dynamicAccountMatch", Type.Special),
			/**
			 * The dynamicAccountSelection variable. The value is used as is.
			 */
			DynamicAccountSelection("dynamicAccountSelection", Type.Special),
			/**
			 * The dynamicVariablePrefix variable.
			 */
			DynamicVariablePrefix("dynamicVariablePrefix"),
			/**
			 * The events. Use {@link OmniVariable.Event} to set any event.
			 */
			Events("events", Type.Complex),
			/**
			 * The fpCookieDomainPeriods variable.
			 */
			FirstPartyCookieDomainPeriods("fpCookieDomainPeriods"),
			/**
			 * The hierarchy variables. Use {@link OmniVariable.Hierarchy} to set any eVar.
			 */
			Hierarchy("hier", Type.Complex),
			/**
			 * The homepage variable. It's read-only: you cannot set it.
			 */
			Homepage("homepage", Type.ReadOnly),
			/**
			 * The javaEnabled variable. It's read-only: you cannot set it.
			 */
			JavaEnabled("javaEnabled", Type.ReadOnly),
			/**
			 * The s_account variable. It's read-only: you cannot set it.
			 */
			JavascriptVersion("javascriptVersion", Type.ReadOnly),
			/**
			 * The linkDownloadFileTypes variable.
			 */
			LinkDownloadFileTypes("linkDownloadFileTypes"),
			/**
			 * The linkExternalFilters variable.
			 */
			LinkExternalFilters("linkExternalFilters"),
			/**
			 * The linkInternalFilters variable.
			 */
			LinkInternalFilters("linkInternalFilters"),
			/**
			 * The linkLeaveQueryString variable. The value is used as is.
			 */
			LinkLeaveQueryString("linkLeaveQueryString", Type.Special),
			/**
			 * The linkName variable.
			 */
			LinkName("linkName"),
			/**
			 * The linkTrackEvents variable. It isn't implemented yet.
			 */
			LinkTrackEvents("linkTrackEvents", Type.Complex),
			/**
			 * The linkTrackVars variable. It isn't implemented yet.
			 */
			LinkTrackVars("linkTrackVars", Type.Complex),
			/**
			 * The linkType variable.
			 */
			LinkType("linkType"),
			/**
			 * The list variables. Use {@link OmniVariable.List} to set any list.
			 */
			List("list", Type.Complex),
			/**
			 * The maxDelay variable.
			 */
			MaxDelay("maxDelay"),
			/**
			 * The Media variable. It isn't implemented yet.
			 */
			Media("Media", Type.Complex),
			/**
			 * The mobile variable.
			 */
			Mobile("mobile"),
			/**
			 * The pageName variable.
			 */
			PageName("pageName"),
			/**
			 * The pageType variable.
			 */
			PageType("pageType"),
			/**
			 * The pageURL variable.
			 */
			PageURL("pageURL"),
			/**
			 * The plugins variable. It's read-only: you cannot set it.
			 */
			Plugins("plugins", Type.ReadOnly),
			/**
			 * The products. Use {@link ProductBean} to set any prop.
			 */
			Products("products", Type.Complex),
			/**
			 * The prop variables. Use {@link OmniVariable.Traffic} to set any prop.
			 */
			Traffic("prop", Type.Complex),
			/**
			 * The purchaseID variable.
			 */
			PurchaseID("purchaseID"),
			/**
			 * The referrer variable.
			 */
			Referrer("referrer"),
			/**
			 * The resolution variable. It's read-only: you cannot set it.
			 */
			Resolution("resolution", Type.ReadOnly),
			/**
			 * The s_objectID variable.
			 */
			ObjectID("s_objectID"),
			/**
			 * The server variable. The value is used as is.
			 */
			Server("server", Type.Special),
			/**
			 * The state variable.
			 */
			State("state"),
			/**
			 * The timestamp variable. The value is used as is.
			 */
			Timestamp("timestamp", Type.Special),
			/**
			 * The trackDownLoadLinks variable. The value is used as is.
			 */
			TrackDownLoadLinks("trackDownLoadLinks", Type.Special),
			/**
			 * The trackExternalLinks variable. The value is used as is.
			 */
			TrackExternalLinks("trackExternalLinks", Type.Special),
			/**
			 * The trackingServer variable.
			 */
			TrackingServer("trackingServer"),
			/**
			 * The trackingServerSecure variable.
			 */
			TrackingServerSecure("trackingServerSecure"),
			/**
			 * The trackInlineStats variable. The value is used as is.
			 */
			TrackInlineStats("trackInlineStats", Type.Special),
			/**
			 * The transactionID variable.
			 */
			TransactionID("transactionID"),
			/**
			 * The s_usePlugins variable. It's read-only: you cannot set it.
			 */
			UsePlugins("s_usePlugins", Type.ReadOnly),
			/**
			 * The visitorID variable.
			 */
			VisitorID("visitorID"),
			/**
			 * The visitorNamespace variable.
			 */
			VisitorNamespace("visitorNamespace"),
			/**
			 * The zip variable.
			 */
			Zip("zip");

			private String name;
			private Type type;

			public String getName() {
				return name;
			}

			public Type getType() {
				return type;
			}

			public String toString() {
				return getName();
			}

			/**
			 * Case insensitive replacement of <tt>valueOf(name)</tt>.
			 *
			 * @param name variable's name in any case
			 * @return named variable
			 * @see Name#valueOf
			 * @throws IllegalArgumentException if a name is not found
			 * @throws NullPointerException if a name is null
			 */
			public static Name forName(final String name) throws IllegalArgumentException {
				Name variable;

				if (null == name) {
					throw new IllegalArgumentException("Variable name is null");
				}

				if (Character.isDigit(name.charAt(name.length() - 1)) && Character.isLetter(name.charAt(0))) {
					variable = NAME_MAP.get(name.split("[0-9]")[0].toLowerCase());
				} else {
					variable = NAME_MAP.get(name.toLowerCase());
				}
				if (null == variable) {
					throw new IllegalArgumentException("Variable name not found: " + name);
				}

				return variable;
			}

			private static final Map<String, Name> NAME_MAP = new HashMap<String, Name>();

			static {
				for (Name namedVariable : Name.values()) {
					NAME_MAP.put(namedVariable.getName().toLowerCase(), namedVariable);
				}
			}

			private Name(String name) {
				this(name, Type.Simple);
			}

			private Name(String name, Type type) {
				this.name = name;
				this.type = type;
			}

			/**
			 * Types of Standard's names.
			 *
			 * @author Vitaliy Berdinskikh
			 */
			public enum Type {
				/**
				 * Use a properly methods to operate with it.
				 */
				Complex,

				/**
				 * You cannot set this variable.
				 */
				ReadOnly,

				/**
				 * A simple string variable.
				 */
				Simple,

				/**
				 * A value is used as is (unquoted).
				 */
				Special;
			}

		}

	}

	/**
	 * Traffic variable prop.
	 *
	 * @author Vitaliy Berdinskikh
	 */
	public static class Traffic extends OmniVariable {

		private static final String PROP = "prop";

		public Traffic(int number) {
			super(number);
		}

		@Override
		public String getPrefix() {
			return PROP;
		}

	}

}