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
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xpath-default-namespace="http://java.sun.com/xml/ns/j2ee" version="2.0">

	<xsl:output indent="yes" method="xml"/>

	<xsl:include href="tags.xsl"/>

	<xsl:template match="attribute()|element()|text()|processing-instruction()">
		<xsl:copy>
			<xsl:apply-templates select="attribute()|element()|text()|comment()|processing-instruction()"/>
		</xsl:copy>
	</xsl:template>

	<xsl:template match="comment()">
		<xsl:text>&#xA;</xsl:text>
		<xsl:copy/>
		<xsl:text>&#xA;</xsl:text>
	</xsl:template>

	<xsl:template match="tag[position()=last()]">
		<xsl:copy>
			<xsl:apply-templates select="attribute()|element()|text()|comment()|processing-instruction()"/>
		</xsl:copy>
		<xsl:call-template name="comment"/>
		<xsl:call-template name="standardVariables"/>
		<xsl:call-template name="nameEvents"/>
		<xsl:call-template name="numberEvents"/>
		<xsl:call-template name="conversionVariables"/>
		<xsl:call-template name="hierarchyVariables"/>
		<xsl:call-template name="listVariables"/>
		<xsl:call-template name="trafficVariables"/>
	</xsl:template>

</xsl:stylesheet>