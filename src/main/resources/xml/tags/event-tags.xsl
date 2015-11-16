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

	<xsl:output indent="yes" method="xml" name="tag" omit-xml-declaration="yes"/>

	<xsl:template match="/">
		<xsl:for-each select="//tag">
			<xsl:variable name="tagNames" select="tokenize(@names, ',')"/>
			<xsl:for-each select="$tagNames">
				<xsl:variable name="tagName" select="."/>
				<xsl:variable name="filename" select="concat($tagName, '.tag')"/>
				<xsl:result-document format="tag" href="{$filename}">
					<xsl:text disable-output-escaping="yes">&#xA;&lt;%@ attribute name='bean' type='ua.co.ur6lad.stroke.OmniBean' rtexprvalue='true' required='false' %&gt;</xsl:text>
					<xsl:text disable-output-escaping="yes">&#xA;&lt;%@ taglib uri='http://ur6lad.co.ua/stroke-taglib' prefix ='s' %&gt;</xsl:text>
					<xsl:text>&#xA;</xsl:text>
					<xsl:text disable-output-escaping="yes">&#xA;&lt;s:event bean='${bean}' name='</xsl:text>
					<xsl:value-of select="$tagName"/>
					<xsl:text disable-output-escaping="yes">'&gt;&lt;jsp:doBody/&gt;&lt;/s:event&gt;</xsl:text>
				</xsl:result-document>
			</xsl:for-each>
		</xsl:for-each>
	</xsl:template>

</xsl:stylesheet>