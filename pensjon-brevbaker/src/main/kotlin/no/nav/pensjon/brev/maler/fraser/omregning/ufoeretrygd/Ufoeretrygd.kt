package no.nav.pensjon.brev.maler.fraser.omregning.ufoeretrygd

import no.nav.pensjon.brev.maler.fraser.Constants
import no.nav.pensjon.brev.maler.fraser.common.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*

object Ufoeretrygd {
    /**
     * TBU3007
     */
    val ungUfoer20aar_001 = createPhrase<LangBokmalNynorsk, KravVirkningFraOgMed> {
        val virkningsdato = argument().format()

        paragraph {
            textExpr(
                Bokmal to "Vi har økt uføretrygden din fra ".expr() + virkningsdato + " fordi du fyller 20 år. Du vil nå få utbetalt uføretrygd med rettighet som ung ufør.",
                Nynorsk to "Vi har auka uføretrygda di frå ".expr() + virkningsdato + " fordi du fyller 20 år. Du får no utbetalt uføretrygd med rett som ung ufør.",
            )
        }
    }

    enum class Tillegg { FELLESBARN, SAERKULLSBARN, EKTEFELLE, GJENLEVENDE }


    /**
     * TBU1120, TBU1121, TBU1122, TBU1254, TBU1253, TBU1123
     */
    data class BeloepPerMaaned(val perMaaned: Kroner, val utbetalteTillegg: Set<Tillegg>)

    val beloep = createPhrase<LangBokmalNynorsk, BeloepPerMaaned> {
        val perMaaned = argument().select(BeloepPerMaaned::perMaaned).str()
        val tillegg = argument().select(BeloepPerMaaned::utbetalteTillegg)

        paragraph {
            showIf(tillegg.isEmpty()) {
                textExpr(
                    Bokmal to "Du får ".expr() + perMaaned + " kroner i uføretrygd per måned før skatt.",
                    Nynorsk to "Du får ".expr() + perMaaned + " kroner i uføretrygd per månad før skatt.",
                )
            }.orShowIf(tillegg.containsExclusively { anyOf(Tillegg.SAERKULLSBARN, Tillegg.FELLESBARN) }) {
                textExpr(
                    Bokmal to "Du får ".expr() + perMaaned + " kroner i uføretrygd og barnetillegg per måned før skatt.",
                    Nynorsk to "Du får ".expr() + perMaaned + " kroner i uføretrygd og barnetillegg per månad før skatt.",
                )
            }.orShowIf(tillegg.containsExclusively { required(Tillegg.GJENLEVENDE) }) {
                textExpr(
                    Bokmal to "Du får ".expr() + perMaaned + " kroner i uføretrygd og gjenlevendetillegg per måned før skatt.",
                    Nynorsk to "Du får ".expr() + perMaaned + " kroner i uføretrygd og attlevandetillegg per månad før skatt.",
                )
            }.orShowIf(tillegg.containsExclusively { required(Tillegg.EKTEFELLE) }) {
                textExpr(
                    Bokmal to "Du får ".expr() + perMaaned + " kroner i uføretrygd og ektefelletillegg per måned før skatt.",
                    Nynorsk to "Du får ".expr() + perMaaned + " kroner i uføretrygd og ektefelletillegg per månad før skatt.",
                )

            }.orShowIf(
                tillegg.containsExclusively {
                    required(Tillegg.EKTEFELLE)
                    anyOf(Tillegg.SAERKULLSBARN, Tillegg.FELLESBARN)
                }
            ) {
                textExpr(
                    Bokmal to "Du får ".expr() + perMaaned + " kroner i uføretrygd, barne- og ektefelletillegg per måned før skatt.",
                    Nynorsk to "Du får ".expr() + perMaaned + " kroner i uføretrygd, barne- og ektefelletillegg per månad før skatt.",
                )

            }.orShowIf(
                tillegg.containsExclusively {
                    required(Tillegg.GJENLEVENDE)
                    anyOf(Tillegg.SAERKULLSBARN, Tillegg.FELLESBARN)
                }
            ) {
                textExpr(
                    Bokmal to "Du får ".expr() + perMaaned + " kroner i uføretrygd, barne- og gjenlevendetillegg per måned før skatt.",
                    Nynorsk to "Du får ".expr() + perMaaned + " kroner i uføretrygd, barne- og attlevandetillegg per månad før skatt.",
                )
            }
        }
    }

    /**
     * TBU1286.1, TBU1286.2
     */
    data class BarnetilleggIkkeUtbetaltDto(val antallFellesbarn: Int, val antallSaerkullsbarn: Int, val inntektstakFellesbarn: Kroner, val inntektstakSaerkullsbarn: Kroner, val utbetalt: Set<Tillegg>, val innvilget: Set<Tillegg>)

    val barnetileggIkkeUtbetalt = createPhrase<LangBokmalNynorsk, BarnetilleggIkkeUtbetaltDto> {
        val innvilget = argument().select(BarnetilleggIkkeUtbetaltDto::innvilget)
        val utbetalt = argument().select(BarnetilleggIkkeUtbetaltDto::utbetalt)

        val saerkullInnvilget = innvilget.containsAny(Tillegg.SAERKULLSBARN)
        val saerkullUtbetalt = utbetalt.containsAny(Tillegg.SAERKULLSBARN)
        val saerkullInntektstak = argument().map { it.inntektstakFellesbarn }.str()

        val fellesInnvilget = innvilget.containsAny(Tillegg.FELLESBARN)
        val fellesUtbetalt = utbetalt.containsAny(Tillegg.FELLESBARN)
        val fellesInntektstak = argument().map { it.inntektstakSaerkullsbarn }.str()

        paragraph {
            showIf(saerkullInnvilget and not(saerkullUtbetalt) and fellesUtbetalt and fellesInnvilget) {

                val barnFlertall = argument().map { it.antallSaerkullsbarn > 1 }
                textExpr(
                    Bokmal to "Barnetillegget for ".expr() + ifElse(barnFlertall, "barna", "barnet") + " som ikke bor sammen med begge foreldrene, blir ikke utbetalt fordi du alene har en samlet inntekt som er høyere enn " +
                            saerkullInntektstak + " kroner. Inntekten din er over grensen for å få utbetalt barnetillegg.",

                    Nynorsk to "Barnetillegget for ".expr() + ifElse(barnFlertall, "barna", "barnet") + "som ikkje bur saman med begge foreldra sine, blir ikkje utbetalt fordi du åleine har ei samla inntekt som er høgare enn " +
                            saerkullInntektstak + " kroner. Inntekta di er over grensa for å få utbetalt barnetillegg.",
                )

            }.orShowIf(saerkullInnvilget and not(saerkullUtbetalt) and not(fellesInnvilget)) {

                textExpr(
                    Bokmal to "Barnetillegget blir ikke utbetalt fordi du har en samlet inntekt som er høyere enn ".expr() +
                            saerkullInntektstak + " kroner. Inntekten din er over grensen for å få utbetalt barnetillegg.",

                    Nynorsk to "Barnetillegget blir ikkje utbetalt fordi du åleine har ei samla inntekt som er høgare enn ".expr() +
                            saerkullInntektstak + " kroner. Inntekten din er over grensen for å få utbetalt barnetillegg.",
                )

            }.orShowIf(fellesInnvilget and not(fellesUtbetalt) and saerkullUtbetalt and saerkullInnvilget) {

                val barnFlertall = argument().map { it.antallFellesbarn > 1 }
                textExpr(
                    Bokmal to "Barnetillegget for ".expr() + ifElse(barnFlertall, "barna", "barnet") + " som bor med begge sine foreldre, blir ikke utbetalt fordi dere har en samlet inntekt som er høyere enn " +
                            fellesInntektstak + " kroner. De samlede inntektene er over grensen for å få utbetalt barnetillegg.",

                    Nynorsk to "Barnetillegget for ".expr() + ifElse(barnFlertall, "barna", "barnet") + " som bur saman med begge foreldra sine, blir ikkje utbetalt fordi dei har ei samla inntekt som er høgare enn " +
                            fellesInntektstak + " kroner. Dei samla inntektene er over grensa for å få utbetalt barnetillegg.",
                )

            }.orShowIf(fellesInnvilget and not(fellesUtbetalt) and not(saerkullInnvilget)) {
                textExpr(
                    Bokmal to "Barnetillegget blir ikke utbetalt fordi dere har en samlet inntekt som er høyere enn ".expr() +
                            fellesInntektstak + " kroner. De samlede inntektene er over grensen for å få utbetalt barnetillegg.",

                    Nynorsk to "Barnetillegget blir ikkje utbetalt fordi dei har ei samla inntekt som er høgare enn ".expr() +
                            fellesInntektstak + " kroner. Dei samla inntektene er over grensa for å få utbetalt barnetillegg.",
                )
            }
        }
    }

    /**
     * TBU1092
     */
    val vedtakBegrunnelseOverskrift = createPhrase<LangBokmalNynorsk, Unit> {
        title1 {
            text(
                Bokmal to "Begrunnelse for vedtaket",
                Nynorsk to "Grunngiving for vedtaket",
            )
        }
    }

    /**
     * TBU3008, TBU3009, TBU3010
     */
    val ungUfoerHoeyereVed20aar = createPhrase<LangBokmalNynorsk, GrunnbeloepSats> {
        paragraph {
            text(
                Bokmal to "Du er tidligere innvilget rettighet som ung ufør i uføretrygden din. Denne rettigheten gir deg høyere utbetaling fra og med den måneden du fyller 20 år.",
                Nynorsk to "Du har tidlegare fått innvilga rett som ung ufør i uføretrygda di. Denne retten gir deg høgare utbetaling frå og med den månaden du fyller 20 år.",
            )
        }

        paragraph {
            val minsteytelseVedVirkSats = argument().format()
            textExpr(
                Bokmal to "Sivilstanden påvirker størrelsen på den årlige uføretrygden og du får derfor en årlig ytelse som utgjør ".expr()
                        + minsteytelseVedVirkSats + " ganger grunnbeløpet.",

                Nynorsk to "Sivilstanden påverkar storleiken på den årlege uføretrygda di, og du får derfor ei årleg yting som utgjer ".expr()
                        + minsteytelseVedVirkSats + " gonger grunnbeløpet.",
            )
        }
    }

    /**
     * TBU3011
     */
    val hjemmelSivilstand = createPhrase<LangBokmalNynorsk, Unit> {
        paragraph {
            text(
                Bokmal to "Vedtaket er gjort etter folketrygdloven § 12-13 og § 22-12.",
                Nynorsk to "Vedtaket har vi gjort etter folketrygdlova § 12-13 og § 22-12.",
            )
        }
    }

    /**
     * TBU1174
     */
    val virkningFomOverskrift = createPhrase<LangBokmalNynorsk, Unit> {
        title1 {
            text(
                Bokmal to "Dette er virkningstidspunktet ditt",
                Nynorsk to "Dette er verknadstidspunktet ditt",
            )
        }
    }

    /**
     * TBU2529x
     */
    val virkningFraOgMed = createPhrase<LangBokmalNynorsk, KravVirkningFraOgMed> {
        val dato = argument().format()
        paragraph {
            textExpr(
                Bokmal to "Uføretrygden din er endret fra ".expr() + dato + ". Dette kaller vi virkningstidspunktet. Du vil derfor få en ny utbetaling fra og med måneden vilkåret er oppfylt.",
                Nynorsk to "Uføretrygda di er endra frå ".expr() + dato + ". Dette kallar vi verknadstidspunktet. Du vil derfor få ny utbetaling frå og med månaden vilkåret er oppfylt.",
            )
        }
    }

    /**
     * TBU1227
     */
    val sjekkUtbetalingene = createPhrase<LangBokmalNynorsk, Unit> {
        title1 {
            text(
                Bokmal to "Sjekk utbetalingene dine",
                Nynorsk to "Sjekk utbetalingane dine",
            )
        }

        paragraph {
            text(
                Bokmal to "Du får uføretrygd utbetalt den 20. hver måned, eller senest siste virkedag før denne datoen. " +
                        "Du kan se alle utbetalingene du har mottatt på ${Constants.DITT_NAV}. Her kan du også endre kontonummeret ditt.",

                Nynorsk to "Du får uføretrygd utbetalt den 20. kvar månad, eller seinast siste yrkedag før denne datoen. " +
                        "Du kan sjå alle utbetalingar du har fått på ${Constants.DITT_NAV}. Her kan du også endre kontonummeret ditt.",
            )
        }
    }
}