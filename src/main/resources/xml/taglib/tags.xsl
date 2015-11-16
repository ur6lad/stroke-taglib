<?xml version="1.0" encoding="UTF-8"?>
<!--

	Copyright 2010-2015 Vitaliy Berdinskikh
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
		http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.

-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" version="2.0">

	<xsl:template name="comment">
		<xsl:text>&#xA;&#xA;&#x9;</xsl:text>
		<xsl:comment> Next tags are auto-generated. </xsl:comment>
	</xsl:template>

	<xsl:template name="standardVariables">
		<xsl:variable name="tagNames" select="tokenize('${standardVariables}', ',')"/>
		<xsl:for-each select="$tagNames">
			<xsl:variable name="tagName" select="."/>
			<xsl:variable name="filename" select="concat('/META-INF/tags/', $tagName, '.tag')"/>
			<xsl:text>&#xA;&#xA;&#x9;</xsl:text>
			<xsl:element name="tag-file" namespace="http://java.sun.com/xml/ns/j2ee">
				<xsl:text>&#xA;&#x9;&#x9;</xsl:text>
				<xsl:element name="name" namespace="http://java.sun.com/xml/ns/j2ee">
					<xsl:value-of select="$tagName"/>
				</xsl:element>
				<xsl:text>&#xA;&#x9;&#x9;</xsl:text>
				<xsl:element name="path" namespace="http://java.sun.com/xml/ns/j2ee">
					<xsl:value-of select="$filename"/>
				</xsl:element>
				<xsl:text>&#xA;&#x9;</xsl:text>
			</xsl:element>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="nameEvents">
		<xsl:variable name="tagNames" select="tokenize('${nameEvents}', ',')"/>
		<xsl:for-each select="$tagNames">
			<xsl:variable name="tagName" select="."/>
			<xsl:variable name="filename" select="concat('/META-INF/tags/', $tagName, '.tag')"/>
			<xsl:text>&#xA;&#xA;&#x9;</xsl:text>
			<xsl:element name="tag-file" namespace="http://java.sun.com/xml/ns/j2ee">
				<xsl:text>&#xA;&#x9;&#x9;</xsl:text>
				<xsl:element name="name" namespace="http://java.sun.com/xml/ns/j2ee">
					<xsl:value-of select="$tagName"/>
				</xsl:element>
				<xsl:text>&#xA;&#x9;&#x9;</xsl:text>
				<xsl:element name="path" namespace="http://java.sun.com/xml/ns/j2ee">
					<xsl:value-of select="$filename"/>
				</xsl:element>
				<xsl:text>&#xA;&#x9;</xsl:text>
			</xsl:element>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="numberEvents">
		<xsl:for-each select="1 to ${eventCount}">
			<xsl:variable name="tagName" select="concat('event', .)"/>
			<xsl:variable name="filename" select="concat('/META-INF/tags/', $tagName, '.tag')"/>
			<xsl:text>&#xA;&#xA;&#x9;</xsl:text>
			<xsl:element name="tag-file" namespace="http://java.sun.com/xml/ns/j2ee">
				<xsl:text>&#xA;&#x9;&#x9;</xsl:text>
				<xsl:element name="name" namespace="http://java.sun.com/xml/ns/j2ee">
					<xsl:value-of select="$tagName"/>
				</xsl:element>
				<xsl:text>&#xA;&#x9;&#x9;</xsl:text>
				<xsl:element name="path" namespace="http://java.sun.com/xml/ns/j2ee">
					<xsl:value-of select="$filename"/>
				</xsl:element>
				<xsl:text>&#xA;&#x9;</xsl:text>
			</xsl:element>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="conversionVariables">
		<xsl:for-each select="1 to ${conversionVariableCount}">
			<xsl:variable name="tagName" select="concat('eVar', .)"/>
			<xsl:variable name="filename" select="concat('/META-INF/tags/', $tagName, '.tag')"/>
			<xsl:text>&#xA;&#xA;&#x9;</xsl:text>
			<xsl:element name="tag-file" namespace="http://java.sun.com/xml/ns/j2ee">
				<xsl:text>&#xA;&#x9;&#x9;</xsl:text>
				<xsl:element name="name" namespace="http://java.sun.com/xml/ns/j2ee">
					<xsl:value-of select="$tagName"/>
				</xsl:element>
				<xsl:text>&#xA;&#x9;&#x9;</xsl:text>
				<xsl:element name="path" namespace="http://java.sun.com/xml/ns/j2ee">
					<xsl:value-of select="$filename"/>
				</xsl:element>
				<xsl:text>&#xA;&#x9;</xsl:text>
			</xsl:element>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="hierarchyVariables">
		<xsl:for-each select="1 to ${hierarchyVariableCount}">
			<xsl:variable name="tagName" select="concat('hier', .)"/>
			<xsl:variable name="filename" select="concat('/META-INF/tags/', $tagName, '.tag')"/>
			<xsl:text>&#xA;&#xA;&#x9;</xsl:text>
			<xsl:element name="tag-file" namespace="http://java.sun.com/xml/ns/j2ee">
				<xsl:text>&#xA;&#x9;&#x9;</xsl:text>
				<xsl:element name="name" namespace="http://java.sun.com/xml/ns/j2ee">
					<xsl:value-of select="$tagName"/>
				</xsl:element>
				<xsl:text>&#xA;&#x9;&#x9;</xsl:text>
				<xsl:element name="path" namespace="http://java.sun.com/xml/ns/j2ee">
					<xsl:value-of select="$filename"/>
				</xsl:element>
				<xsl:text>&#xA;&#x9;</xsl:text>
			</xsl:element>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="listVariables">
		<xsl:for-each select="1 to ${listVariableCount}">
			<xsl:variable name="tagName" select="concat('list', .)"/>
			<xsl:variable name="filename" select="concat('/META-INF/tags/', $tagName, '.tag')"/>
			<xsl:text>&#xA;&#xA;&#x9;</xsl:text>
			<xsl:element name="tag-file" namespace="http://java.sun.com/xml/ns/j2ee">
				<xsl:text>&#xA;&#x9;&#x9;</xsl:text>
				<xsl:element name="name" namespace="http://java.sun.com/xml/ns/j2ee">
					<xsl:value-of select="$tagName"/>
				</xsl:element>
				<xsl:text>&#xA;&#x9;&#x9;</xsl:text>
				<xsl:element name="path" namespace="http://java.sun.com/xml/ns/j2ee">
					<xsl:value-of select="$filename"/>
				</xsl:element>
				<xsl:text>&#xA;&#x9;</xsl:text>
			</xsl:element>
		</xsl:for-each>
	</xsl:template>

	<xsl:template name="trafficVariables">
		<xsl:for-each select="1 to ${trafficVariableCount}">
			<xsl:variable name="tagName" select="concat('prop', .)"/>
			<xsl:variable name="filename" select="concat('/META-INF/tags/', $tagName, '.tag')"/>
			<xsl:text>&#xA;&#xA;&#x9;</xsl:text>
			<xsl:element name="tag-file" namespace="http://java.sun.com/xml/ns/j2ee">
				<xsl:text>&#xA;&#x9;&#x9;</xsl:text>
				<xsl:element name="name" namespace="http://java.sun.com/xml/ns/j2ee">
					<xsl:value-of select="$tagName"/>
				</xsl:element>
				<xsl:text>&#xA;&#x9;&#x9;</xsl:text>
				<xsl:element name="path" namespace="http://java.sun.com/xml/ns/j2ee">
					<xsl:value-of select="$filename"/>
				</xsl:element>
				<xsl:text>&#xA;&#x9;</xsl:text>
			</xsl:element>
		</xsl:for-each>
	</xsl:template>

</xsl:stylesheet>