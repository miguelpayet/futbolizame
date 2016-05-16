<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:variable name="smallcase" select="'abcdefghijklmnopqrstuvwxyz'"/>
    <xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'"/>

    <xsl:template match="/data">
        <list>
            <xsl:apply-templates select="//ul"/>
        </list>
    </xsl:template>

    <xsl:template match="//ul">
        <fecha idfecha="{300+position()}">
            <numero>
                <xsl:value-of select="position()"/>
            </numero>
            <nombre>Jornada&#160;<xsl:value-of select="position()"/>
            </nombre>
            <idconcurso>1</idconcurso>
            <xsl:apply-templates select=".//em[1][@class='fecha']" mode="fecha"/>
        </fecha>
    </xsl:template>

    <xsl:template match="//em" mode="fecha">
        <xsl:variable name="f" select="span[@class='date']"/>
        <fecha>
            <xsl:value-of select="concat(substring($f,7,4),'-',substring($f,4,2),'-',substring($f,1,2))"/>
        </fecha>
    </xsl:template>

</xsl:stylesheet>