<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="/">
		<list>
			<xsl:apply-templates select="//div[@class='mu result']"/>
		</list>
	</xsl:template>

	<xsl:template match="//div[@class='mu result']">
		<xsl:variable name="dia" select="substring(div[@class='mu-i']/div[@class='mu-i-date'], 1, 2)"/>
		<xsl:variable name="año" select="substring(div[@class='mu-i']/div[@class='mu-i-date'], 8, 4)"/>
		<xsl:variable name="mes">
			<xsl:call-template name="mes">
				<xsl:with-param name="month" select="substring(div[@class='mu-i']/div[@class='mu-i-date'], 4, 3)"/>
			</xsl:call-template>
		</xsl:variable>
		<partido idpartido="{@data-id}">
			<idfecha>
				<xsl:value-of select="concat($año, $mes, $dia)"/>
			</idfecha>
			<fecha>
				<xsl:value-of select="concat($año, '.', $mes, '.', $dia)"/>
			</fecha>
			<numero>
				<xsl:value-of select="position()"/>
			</numero>
		</partido>
	</xsl:template>

	<xsl:template name="mes">
		<xsl:param name="month"/>
		<!--xsl:value-of select="$month"/-->
		<xsl:choose>
			<xsl:when test="$month='Jan'">01</xsl:when>
			<xsl:when test="$month='Feb'">02</xsl:when>
			<xsl:when test="$month='Mar'">03</xsl:when>
			<xsl:when test="$month='Apr'">04</xsl:when>
			<xsl:when test="$month='May'">05</xsl:when>
			<xsl:when test="$month='Jun'">06</xsl:when>
			<xsl:when test="$month='Jul'">07</xsl:when>
			<xsl:when test="$month='Aug'">08</xsl:when>
			<xsl:when test="$month='Sep'">09</xsl:when>
			<xsl:when test="$month='Oct'">10</xsl:when>
			<xsl:when test="$month='Nov'">11</xsl:when>
			<xsl:when test="$month='Dec'">12</xsl:when>
			<xsl:otherwise>00</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>
