<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:variable name="smallcase" select="'abcdefghijklmnopqrstuvwxyz'"/>
	<xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'"/>

	<xsl:template match="/">
		<list>
			<xsl:apply-templates select="//div[@class='mu result']"/>
		</list>
	</xsl:template>

	<xsl:template match="//div[@class='mu result']">
		<xsl:apply-templates select="div[@class='mu-m']" mode="local">
			<xsl:with-param name="partido" select="@data-id"/>
		</xsl:apply-templates>
		<xsl:apply-templates select="div[@class='mu-m']" mode="visita">
			<xsl:with-param name="partido" select="@data-id"/>
		</xsl:apply-templates>
	</xsl:template>

	<xsl:template match="//div[@class='mu result']/div[@class='mu-m']" mode="local">
		<xsl:param name="partido"/>
		<participacion>
			<idpartido>
				<xsl:value-of select="$partido"/>
			</idpartido>
			<xsl:apply-templates select="div[@class='t home']/div[@class='t-n']"/>
			<goles>
				<xsl:value-of select="substring-before(div[@class='s']//div[@class='s-score s-date-HHmm']/span, '-')"/>
			</goles>
		</participacion>
	</xsl:template>

	<xsl:template match="//div[@class='mu result']/div[@class='mu-m']" mode="visita">
		<xsl:param name="partido"/>
		<participacion>
			<idpartido>
				<xsl:value-of select="$partido"/>
			</idpartido>
			<xsl:apply-templates select="div[@class='t away']/div[@class='t-n']"/>
			<goles>
				<xsl:value-of select="substring-after(div[@class='s']//div[@class='s-score s-date-HHmm']/span, '-')"/>
			</goles>
		</participacion>
	</xsl:template>

	<xsl:template match="//div[@class='mu result']/div[@class='mu-m']/div[@class='t away']/div[@class='t-n']">
		<local>0</local>
		<idequipo>
			<xsl:value-of select="translate(span[@class='t-nText '], $uppercase, $smallcase)"/>
		</idequipo>
	</xsl:template>

	<xsl:template match="//div[@class='mu result']/div[@class='mu-m']/div[@class='t home']/div[@class='t-n']">
		<local>1</local>
		<idequipo>
			<xsl:value-of select="translate(span[@class='t-nText '], $uppercase, $smallcase)"/>
		</idequipo>
	</xsl:template>

</xsl:stylesheet>
