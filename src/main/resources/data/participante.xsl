<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:variable name="smallcase" select="'abcdefghijklmnopqrstuvwxyz'"/>
    <xsl:variable name="uppercase" select="'ABCDEFGHIJKLMNOPQRSTUVWXYZ'"/>

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
        <xsl:variable name="pospar" select="($pos * 10) + position()"/>
        <participacion>
            <idpartido>
                <xsl:value-of select="$pospar"/>
            </idpartido>
            <xsl:apply-templates select=".//a[contains(@class,'a-rg')]" mode="local"/>
            <xsl:apply-templates select=".//a[@class='res']" mode="scorelocal"/>
        </participacion>
        <participacion>
            <idpartido>
                <xsl:value-of select="$pospar"/>
            </idpartido>
            <xsl:apply-templates select=".//a[contains(@class,'a-lf')]" mode="visita"/>
            <xsl:apply-templates select=".//a[@class='res']" mode="scorevisita"/>
        </participacion>
    </xsl:template>

    <xsl:template match="//a" mode="local">
        <idequipo>
            <xsl:value-of select="translate(em[@class='e'], $uppercase, $smallcase)"/>
        </idequipo>
        <local>1</local>
    </xsl:template>

    <xsl:template match="//a" mode="visita">
        <idequipo>
            <xsl:value-of select="translate(em[@class='e'], $uppercase, $smallcase)"/>
        </idequipo>
        <local>0</local>
    </xsl:template>

    <xsl:template match="//a" mode="scorelocal">
        <goles>
            <xsl:value-of select="substring-before(em[@class='LiveHora'], ' - ')"/>
        </goles>
    </xsl:template>

    <xsl:template match="//a" mode="scorevisita">
        <goles>
            <xsl:value-of select="substring-after(em[@class='LiveHora'], ' - ')"/>
        </goles>
    </xsl:template>

</xsl:stylesheet>