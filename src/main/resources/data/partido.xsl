<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="/data">
        <list>
            <xsl:apply-templates select="//ul"/>
        </list>
    </xsl:template>

    <xsl:template match="//ul">
        <xsl:variable name="pos" select="300+position()"/>
        <xsl:apply-templates select="li" mode="partido">
            <xsl:with-param name="pos" select="$pos"/>
        </xsl:apply-templates>
    </xsl:template>

    <xsl:template match="//li" mode="partido">
        <xsl:param name="pos"/>
        <partido idpartido="{($pos * 10) + position()}">
            <idfecha>
                <xsl:value-of select="$pos"/>
            </idfecha>
            <xsl:apply-templates select=".//em[@class='fecha']" mode="fecha"/>
        </partido>
    </xsl:template>

    <xsl:template match="//em" mode="fecha">
        <xsl:variable name="f" select="span[@class='date']"/>
        <fecha>
            <xsl:value-of select="concat(substring($f,7,4),'-',substring($f,4,2),'-',substring($f,1,2))"/>
        </fecha>
    </xsl:template>

</xsl:stylesheet>