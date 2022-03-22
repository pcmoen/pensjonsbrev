package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.maler.fraser.common.Kroner
import no.nav.pensjon.brev.template.Element.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import java.time.LocalDate

// TODO: Lag gjenbrukbar formattering av Kroner(1000) med språkstøtte og enhet (kr/NOK)

data class OpplysningerBruktIBeregningUTDto(
    val anvendtTT_trygdetidsdetaljerGjeldende: Int,
    val avkortningsbelopAr_barnetilleggSBGjeldende: Int,
    val belop_barnetilleggSBGjeldende: Int,
    val belopAr_barnetilleggSBGjeldende: Int,
    val belopArForAvkort_barnetilleggSBGjeldende: Int,
    val belopIEU_inntektEtterUforeGjeldende: Int,
    val belopsgrense_uforetrygdGjeldende: Int,
    val beregningsgrunnlagBelopAr_uforetrygdGjeldende: Int,
    val beregningsgrunnlagBelopAr_yrkesskadeGjeldende: Int,
    val brukersSivilstand_gjeldendeBeregnetUTPerManed: String,
    val faktiskTTBilateral_trygdetidsdetaljerGjeldende: Int,
    val faktiskTTEOS_trygdetidsdetaljerGjeldende: Int,
    val faktiskTTNordiskKonv_trygdetidsdetaljerGjeldende: Int,
    val faktiskTTNorge_trygdetidsdetaljerGjeldende: Int,
    val forventetInntektAr_inntektsAvkortingGjeldende: Int,
    val framtidigTTNorsk_trygdetidsdetaljerGjeldende: Int,
    val fribelop_barnetilleggSBGjeldende: Int,
    val gradertOIFU_barnetilleggGrunnlagGjeldende: Int,
    val grunnbelop_gjeldendeBeregnetUTPerManed: Int,
    val ifuInntekt_inntektForUforeGjeldende: Int,
    val inntektBruktIAvkortning_barnetilleggSBGjeldende: Int,
    val inntektstak_barnetilleggSBGjeldende: Int,
    val inntektsgrenseAr_inntektsAvkortingGjeldende: Int,
    val inntektstak_inntektsAvkortingGjeldende: Int,
    val inntektVedSkadetidspunkt_yrkesskadeGjeldende: Int,
    val justeringsbelopAr_barnetilleggSBGjeldende: Int,
    val kompensasjonsgrad_uforetrygdGjeldende: Double,
    val nevnerProRata_trygdetidsdetaljerGjeldende: Int,
    val nevnerTTEOS_trygdetidsdetaljerGjeldende: Int,
    val nevnerTTNordiskKonv_trygdetidsdetaljerGjeldende: Int,
    val prosentsatsGradertOIFU_barnetilleggGrunnlagGjeldende: Int,
    val samletTTNordiskKonv_trygdetidsdetaljerGjeldende: Int,
    val skadetidspunkt_yrkesskadeGjeldende: LocalDate,
    val tellerProRata_trygdetidsdetaljerGjeldende: Int,
    val tellerTTEOS_trygdetidsdetaljerGjeldende: Int,
    val tellerTTNordiskKonv_trygdetidsdetaljerGjeldende: Int,
    val totaltAntallBarn_barnetilleggGrunnlagGjeldende: Int,
    val uforegrad_uforetrygdGjeldende: Int,
    val uforetidspunkt_uforetrygdGjeldende: LocalDate,
    val virkDatoFom_gjeldendeBeregnetUTPerManed: LocalDate,
    val yrkesskadegrad_yrkesskadeGjeldende: Int,
)

val opplysningerBruktIBeregningUT = createAttachment<LangBokmalNynorskEnglish, OpplysningerBruktIBeregningUTDto>(
    title = newText(
        Bokmal to "Opplysninger om beregningen",
        Nynorsk to "Opplysningar om utrekninga",
        English to "Information about calculations"
    ),
    includeSakspart = false,
) {
    paragraph {
        val virkDatoFom =
            argument().map { it.virkDatoFom_gjeldendeBeregnetUTPerManed }.format()
        val grunnbelop = argument().map { Kroner(it.grunnbelop_gjeldendeBeregnetUTPerManed) }.format()
        textExpr(
            Bokmal to "Opplysninger vi har brukt i beregningen fra ".expr() + virkDatoFom.str() + " Folketrygdens grunnbeløp (G) benyttet i beregningen er ".expr() + grunnbelop.str() + " kroner",
            Nynorsk to "Opplysningar vi har brukt i utrekninga frå ".expr() + virkDatoFom.str() + " Grunnbeløpet i folketrygda (G) nytta i utrekninga er ".expr() + grunnbelop.str() + " kroner",
            English to "Data we have used in the calculations as of ".expr() + virkDatoFom.str() + " The National Insurance basic amount (G) used in the calculation is NOK ".expr() + grunnbelop.str() + "."
        )
    }

    table(
        header = {
            column(3) {
                text(
                    Bokmal to "Opplysning", // TODO
                    Nynorsk to "Opplysning", // TODO
                    English to "Information" // TODO
                )
            }
            column(alignment = RIGHT) {
                text(
                    Bokmal to "Verdi", // TODO
                    Nynorsk to "Verdi", // TODO
                    English to "Value" // TODO
                )
            }
        }
    ) {
        row {
            cell {
                text(
                    Bokmal to "Uføretidspunkt",
                    Nynorsk to "Uføretidspunkt",
                    English to "Date of disability"
                )
            }
            cell {
                val uforetidspunkt =
                    argument().map { it.uforetidspunkt_uforetrygdGjeldende }.format()
                textExpr(
                    Bokmal to uforetidspunkt,
                    Nynorsk to uforetidspunkt,
                    English to uforetidspunkt
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Beregningsgrunnlag",
                    Nynorsk to "Utrekningsgrunnlag",
                    English to "Basis for calculation"
                )
            }
            cell {
                val beregningsgrunnlagBelopAr =
                    argument().map { Kroner(it.beregningsgrunnlagBelopAr_uforetrygdGjeldende) }.format()
                textExpr(
                    Bokmal to beregningsgrunnlagBelopAr + " kr",
                    Nynorsk to beregningsgrunnlagBelopAr + " kr",
                    English to beregningsgrunnlagBelopAr + " NOK"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Beregningsgrunnlag yrkesskade",
                    Nynorsk to "Utrekningsgrunnlag yrkesskade",
                    English to "Basis for calculation due to occupational injury"
                )
            }
            cell {
                val beregningsgrunnlagBelopAr =
                    argument().map { Kroner(it.beregningsgrunnlagBelopAr_yrkesskadeGjeldende) }.format()
                textExpr(
                    Bokmal to beregningsgrunnlagBelopAr + " kr",
                    Nynorsk to beregningsgrunnlagBelopAr + " kr",
                    English to beregningsgrunnlagBelopAr + " NOK"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Inntekt før uførhet",
                    Nynorsk to "Inntekt før uførleik",
                    English to "Income prior to disability"
                )
            }
            cell {
                val ifuInntekt =
                    argument().map { Kroner(it.ifuInntekt_inntektForUforeGjeldende) }.format()
                textExpr(
                    Bokmal to ifuInntekt + " kr",
                    Nynorsk to ifuInntekt + " kr",
                    English to ifuInntekt + " NOK"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Inntekt etter uførhet",
                    Nynorsk to "Inntekt etter uførleik",
                    English to "Income after disability"
                )
            }
            cell {
                val belopIEU =
                    argument().map { Kroner(it.belopIEU_inntektEtterUforeGjeldende) }.format()
                textExpr(
                    Bokmal to belopIEU + " kr",
                    Nynorsk to belopIEU + " kr",
                    English to belopIEU + " NOK"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Uføregrad",
                    Nynorsk to "Uføregrad",
                    English to "Degree of disability"
                )
            }
            cell {
                val uforegrad = argument().map { it.uforegrad_uforetrygdGjeldende }.str()
                textExpr(
                    Bokmal to uforegrad + " %",
                    Nynorsk to uforegrad + " %",
                    English to uforegrad + " %"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Inntektsgrense",
                    Nynorsk to "Inntektsgrense",
                    English to "Income cap"
                )
            }
            cell {
                val belopsgrense =
                    argument().map { Kroner(it.belopsgrense_uforetrygdGjeldende) }.format()
                textExpr(
                    Bokmal to belopsgrense + " kr",
                    Nynorsk to belopsgrense + " kr",
                    English to belopsgrense + " NOK"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Inntektsgrense",
                    Nynorsk to "Inntektsgrense",
                    English to "Income cap"
                )
            }
            cell {
                val inntektsgrenseAr =
                    argument().map { Kroner(it.inntektsgrenseAr_inntektsAvkortingGjeldende) }
                        .format()
                textExpr(
                    Bokmal to inntektsgrenseAr + " kr",
                    Nynorsk to inntektsgrenseAr + " kr",
                    English to inntektsgrenseAr + " NOK"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Forventet inntekt",
                    Nynorsk to "Forventa inntekt",
                    English to "Expected income"
                )
            }
            cell {
                val forventetInntektAr =
                    argument().map { Kroner(it.forventetInntektAr_inntektsAvkortingGjeldende) }
                        .format()
                textExpr(
                    Bokmal to forventetInntektAr + " kr",
                    Nynorsk to forventetInntektAr + " kr",
                    English to forventetInntektAr + " NOK"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Kompensasjonsgrad",
                    Nynorsk to "Kompensasjonsgrad",
                    English to "Percentage of compensation"
                )
            }
            cell {
                val kompensasjonsgrad =
                    argument().map { it.kompensasjonsgrad_uforetrygdGjeldende }.format()
                textExpr(
                    Bokmal to kompensasjonsgrad + " %",
                    Nynorsk to kompensasjonsgrad + " %",
                    English to kompensasjonsgrad + " %"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Inntekt som medfører at uføretrygden ikke blir utbetalt",
                    Nynorsk to "Inntekt som fører til at uføretrygda ikkje blir utbetalt",
                    English to "Income that will lead to no payment of your disability benefit"
                )
            }
            cell {
                val inntektstak =
                    argument().map { Kroner(it.inntektstak_inntektsAvkortingGjeldende) }.format()
                textExpr(
                    Bokmal to inntektstak + " kr",
                    Nynorsk to inntektstak + " kr",
                    English to inntektstak + " NOK"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Inntekt som medfører at uføretrygden ikke blir utbetalt",
                    Nynorsk to "Inntekt som fører til at uføretrygda ikkje blir utbetalt",
                    English to "Income that will lead to no payment of your disability benefit"
                )
            }
            cell {
                val inntektsgrenseAr =
                    argument().map { Kroner(it.inntektsgrenseAr_inntektsAvkortingGjeldende) }
                        .format()
                textExpr(
                    Bokmal to inntektsgrenseAr + " kr",
                    Nynorsk to inntektsgrenseAr + " kr",
                    English to inntektsgrenseAr + " NOK"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Sivilstatus lagt til grunn i beregningen",
                    Nynorsk to "Sivilstatus lagt til grunn i utrekninga",
                    English to "Marital status applied to calculation"
                )
            }
            cell {
                val brukersSivilstand =
                    argument().map { it.brukersSivilstand_gjeldendeBeregnetUTPerManed }
                        .str()
                textExpr(
                    Bokmal to brukersSivilstand,
                    Nynorsk to brukersSivilstand,
                    English to brukersSivilstand
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Ung ufør",
                    Nynorsk to "Ung ufør",
                    English to "Young disabled"
                )
            }
            cell {
                text(
                    Bokmal to "Ja",
                    Nynorsk to "Ja",
                    English to "Yes"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Yrkesskadegrad",
                    Nynorsk to "Yrkesskadegrad",
                    English to "Degree of disability due to occupational injury"
                )
            }
            cell {
                val yrkesskadegrad =
                    argument().map { it.yrkesskadegrad_yrkesskadeGjeldende }.str()
                textExpr(
                    Bokmal to yrkesskadegrad + " %",
                    Nynorsk to yrkesskadegrad + " %",
                    English to yrkesskadegrad + " %"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Skadetidspunktet for yrkesskaden",
                    Nynorsk to "Skadetidspunktet for yrkesskaden",
                    English to "Date of injury"
                )
            }
            cell {
                val skadetidspunkt =
                    argument().map { it.skadetidspunkt_yrkesskadeGjeldende }.format()
                textExpr(
                    Bokmal to skadetidspunkt,
                    Nynorsk to skadetidspunkt,
                    English to skadetidspunkt
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Årlig arbeidsinntekt på skadetidspunktet",
                    Nynorsk to "Årleg arbeidsinntekt på skadetidspunktet",
                    English to "Annual income at the date of injury"
                )
            }
            cell {
                val inntektVedSkadetidspunkt =
                    argument().map { Kroner(it.inntektVedSkadetidspunkt_yrkesskadeGjeldende) }
                        .format()
                textExpr(
                    Bokmal to inntektVedSkadetidspunkt + " kr",
                    Nynorsk to inntektVedSkadetidspunkt + " kr",
                    English to inntektVedSkadetidspunkt + " NOK"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Du er innvilget flyktningstatus fra UDI",
                    Nynorsk to "Du er innvilga flyktningstatus frå UDI",
                    English to "You have been granted status as a refugee by the Norwegian Directorate of Immigration (UDI)"
                )
            }
            cell {
                text(
                    Bokmal to "Ja",
                    Nynorsk to "Ja",
                    English to "Yes"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Trygdetid (maksimalt 40 år)",
                    Nynorsk to "Trygdetid (maksimalt 40 år)",
                    English to "Insurance period (maximum 40 years)"
                )
            }
            // Implement logic for year/years
            cell {
                val anvendtTT =
                    argument().map { it.anvendtTT_trygdetidsdetaljerGjeldende }.str()
                textExpr(
                    Bokmal to anvendtTT + " år",
                    Nynorsk to anvendtTT + " år",
                    English to anvendtTT + " years"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Teoretisk trygdetid i Norge og andre EØS-land som er brukt i beregningen (maksimalt 40 år)",
                    Nynorsk to "Teoretisk trygdetid i Noreg og andre EØS-land som er brukt i utrekninga (maksimalt 40 år)",
                    English to "Theoretical insurance period in Norway and other EEA countries used in the calculation (maximum 40 years)"
                )
            }
            // Implement logic for year/years
            cell {
                val anvendtTT =
                    argument().map { it.anvendtTT_trygdetidsdetaljerGjeldende }.str()
                textExpr(
                    Bokmal to anvendtTT + " år",
                    Nynorsk to anvendtTT + " år",
                    English to anvendtTT + " years"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Teoretisk trygdetid i Norge og andre avtaleland som er brukt i beregningen (maksimalt 40 år)",
                    Nynorsk to "Teoretisk trygdetid i Noreg og andre avtaleland som er brukt i utrekninga (maksimalt 40 år)",
                    English to "Theoretical insurance period in Norway and other partner countries used in the calculation (maximum 40 years)"
                )
            }
            cell {
                val anvendtTT =
                    argument().map { it.anvendtTT_trygdetidsdetaljerGjeldende }.str()
                textExpr(
                    Bokmal to anvendtTT + " år",
                    Nynorsk to anvendtTT + " år",
                    English to anvendtTT + " years"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Faktisk trygdetid i Norge",
                    Nynorsk to "Faktisk trygdetid i Noreg",
                    English to "Actual insurance period in Norway"
                )
            }
            cell {
                val faktiskTTNorge =
                    argument().map { it.faktiskTTNorge_trygdetidsdetaljerGjeldende }
                        .str()
                textExpr(
                    Bokmal to faktiskTTNorge + " måneder",
                    Nynorsk to faktiskTTNorge + " måneder",
                    English to faktiskTTNorge + " months"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Faktisk trygdetid i andre EØS-land",
                    Nynorsk to "Faktisk trygdetid i andre EØS-land",
                    English to "Actual insurance period(s) in other EEA countries"
                )
            }
            cell {
                val faktiskTTEOS =
                    argument().map { it.faktiskTTEOS_trygdetidsdetaljerGjeldende }.str()
                textExpr(
                    Bokmal to faktiskTTEOS + " måneder",
                    Nynorsk to faktiskTTEOS + " måneder",
                    English to faktiskTTEOS + " months"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Faktisk trygdetid i Norge og EØS-land (maksimalt 40 år)",
                    Nynorsk to "Faktisk trygdetid i Noreg og EØS-land (maksimalt 40 år)",
                    English to "Actual insurance period in Norway and EEA countries (maximum 40 years)"
                )
            }
            cell {
                val nevnerTTEOS =
                    argument().map { it.nevnerTTEOS_trygdetidsdetaljerGjeldende }.str()
                textExpr(
                    Bokmal to nevnerTTEOS + " måneder",
                    Nynorsk to nevnerTTEOS + " måneder",
                    English to nevnerTTEOS + " months"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Forholdstallet brukt i beregning av trygdetid",
                    Nynorsk to "Forholdstalet brukt i utrekning av trygdetid",
                    English to "Ratio applied in calculation of insurance period"
                )
            }
            cell {
                val tellerTTEOS =
                    argument().map { it.tellerTTEOS_trygdetidsdetaljerGjeldende }.str()
                val nevnerTTEOS =
                    argument().map { it.nevnerTTEOS_trygdetidsdetaljerGjeldende }.str()
                textExpr(
                    Bokmal to tellerTTEOS + " / " + nevnerTTEOS,
                    Nynorsk to tellerTTEOS + " / " + nevnerTTEOS,
                    English to tellerTTEOS + " / " + nevnerTTEOS
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Faktisk trygdetid i annet nordisk land som brukes i beregning av framtidig trygdetid",
                    Nynorsk to "Faktisk trygdetid i anna nordisk land som blir brukt i utrekning av framtidig trygdetid",
                    English to "Actual insurance period in another Nordic country, applied in calculation of future insurance period(s)"
                )
            }
            cell {
                val faktiskTTNordiskKonv =
                    argument().map { it.faktiskTTNordiskKonv_trygdetidsdetaljerGjeldende }
                        .str()
                textExpr(
                    Bokmal to faktiskTTNordiskKonv + " måneder",
                    Nynorsk to faktiskTTNordiskKonv + " måneder",
                    English to faktiskTTNordiskKonv + " months"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Norsk framtidig trygdetid",
                    Nynorsk to "Norsk framtidig trygdetid",
                    English to "Future insurance period in Norway"
                )
            }
            cell {
                val framtidigTTNorsk =
                    argument().map { it.framtidigTTNorsk_trygdetidsdetaljerGjeldende }
                        .str()
                textExpr(
                    Bokmal to framtidigTTNorsk + " måneder",
                    Nynorsk to framtidigTTNorsk + " måneder",
                    English to framtidigTTNorsk + " months"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Forholdstallet brukt i reduksjon av norsk framtidig trygdetid",
                    Nynorsk to "Forholdstalet brukt i reduksjon av norsk framtidig trygdetid",
                    English to "Ratio applied in reduction of future Norwegian insurance period"
                )
            }
            cell {
                val tellerTTNordiskKonv =
                    argument().map { it.tellerTTNordiskKonv_trygdetidsdetaljerGjeldende }
                        .str()
                val nevnerTTNordiskKonv_trygdetidsdetaljerGjeldende =
                    argument().map { it.nevnerTTNordiskKonv_trygdetidsdetaljerGjeldende }
                        .str()
                textExpr(
                    Bokmal to tellerTTNordiskKonv + " / " + nevnerTTNordiskKonv_trygdetidsdetaljerGjeldende,
                    Nynorsk to tellerTTNordiskKonv + " / " + nevnerTTNordiskKonv_trygdetidsdetaljerGjeldende,
                    English to tellerTTNordiskKonv + " / " + nevnerTTNordiskKonv_trygdetidsdetaljerGjeldende
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Samlet trygdetid brukt i beregning av uføretrygd etter reduksjon av framtidig trygdetid",
                    Nynorsk to "Samla trygdetid brukt i utrekning av uføretrygd etter reduksjon av framtidig trygdetid",
                    English to "Total insurance period applied in calculating disability benefit after reduction of future insurance period(s"
                )
            }
            cell {
                val samletTTNordiskKonv =
                    argument().map { it.samletTTNordiskKonv_trygdetidsdetaljerGjeldende }
                        .str()
                textExpr(
                    Bokmal to samletTTNordiskKonv + " måneder",
                    Nynorsk to samletTTNordiskKonv + " måneder",
                    English to samletTTNordiskKonv + " months"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Faktisk trygdetid i annet avtaleland",
                    Nynorsk to "Faktisk trygdetid i anna avtaleland",
                    English to "Actual insurance period(s) in another partner country"
                )
            }
            cell {
                val faktiskTTBilateral =
                    argument().map { it.faktiskTTBilateral_trygdetidsdetaljerGjeldende }
                        .str()
                textExpr(
                    Bokmal to faktiskTTBilateral + " måneder",
                    Nynorsk to faktiskTTBilateral + " måneder",
                    English to faktiskTTBilateral + " months"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Faktisk trygdetid i Norge og avtaleland (maksimalt 40 år)",
                    Nynorsk to "Faktisk trygdetid i Noreg og avtaleland (maksimalt 40 år)",
                    English to "Actual insurance period in Norway and partner countries (maximum 40 years)"
                )
            }
            cell {
                val nevnerProRata =
                    argument().map { it.nevnerProRata_trygdetidsdetaljerGjeldende }.str()
                textExpr(
                    Bokmal to nevnerProRata + " måneder",
                    Nynorsk to nevnerProRata + " måneder",
                    English to nevnerProRata + " months"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Forholdstallet brukt i beregning av uføretrygd",
                    Nynorsk to "Forholdstalet brukt i utrekning av uføretrygd",
                    English to "Ratio applied in calculation of insurance period"
                )
            }
            cell {
                val tellerProRata =
                    argument().map { it.tellerProRata_trygdetidsdetaljerGjeldende }.str()
                val nevnerProRata =
                    argument().map { it.nevnerProRata_trygdetidsdetaljerGjeldende }.str()
                textExpr(
                    Bokmal to tellerProRata + " / " + nevnerProRata,
                    Nynorsk to tellerProRata + " / " + nevnerProRata,
                    English to tellerProRata + " / " + nevnerProRata
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Totalt antall barn du har barnetillegg for",
                    Nynorsk to "Totalt tal barn du har barnetillegg for",
                    English to "Total number of children for whom you receive child supplement"
                )
            }
            cell {
                val totaltAntallBarn =
                    argument().map { it.totaltAntallBarn_barnetilleggGrunnlagGjeldende }
                        .str()
                textExpr(
                    Bokmal to totaltAntallBarn,
                    Nynorsk to totaltAntallBarn,
                    English to totaltAntallBarn
                )
            }
        }
        row {
            cell {
                val prosentsatsGradertOIFU =
                    argument().map { it.prosentsatsGradertOIFU_barnetilleggGrunnlagGjeldende }
                        .str()
                textExpr(
                    Bokmal to prosentsatsGradertOIFU + " % av inntekt før uførhet (justert for endringer i grunnbeløpet)",
                    Nynorsk to prosentsatsGradertOIFU + " % av inntekt før uførleik (justert for endringar i grunnbeløpet)",
                    English to prosentsatsGradertOIFU + " % of income before disability, adjusted for changes in the basic amount"
                )
            }
            cell {
                val gradertOIFU =
                    argument().map { Kroner(it.gradertOIFU_barnetilleggGrunnlagGjeldende) }
                        .format()
                textExpr(
                    Bokmal to gradertOIFU + " kr",
                    Nynorsk to gradertOIFU + " kr",
                    English to gradertOIFU + " NOK"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Fribeløp for særkullsbarn",
                    Nynorsk to "Fribeløp for særkullsbarn",
                    English to "Exemption amount for children from a previous relationship"
                )
            }
            cell {
                val fribelop =
                    argument().map { Kroner(it.fribelop_barnetilleggSBGjeldende) }.format()
                textExpr(
                    Bokmal to fribelop + " kr",
                    Nynorsk to fribelop + " kr",
                    English to fribelop + " NOK"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Samlet inntekt som er brukt i fastsettelse av barnetillegg",
                    Nynorsk to "Samla inntekt som er brukt i fastsetjinga av barnetillegg",
                    English to "Your income, which is used to calculate child supplement"
                )
            }
            cell {
                val inntektBruktIAvkortning =
                    argument().map { Kroner(it.inntektBruktIAvkortning_barnetilleggSBGjeldende) }
                        .format()
                textExpr(
                    Bokmal to inntektBruktIAvkortning + " kr",
                    Nynorsk to inntektBruktIAvkortning + " kr",
                    English to inntektBruktIAvkortning + " NOK"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Samlet inntekt for deg som gjør at barnetillegget ikke blir utbetalt",
                    Nynorsk to "Samla inntekt for deg som gjer at barnetillegget ikkje blir utbetalt",
                    English to "Your income which means that no child supplement is received"
                )
            }
            cell {
                val inntektstak =
                    argument().map { Kroner(it.inntektstak_barnetilleggSBGjeldende) }.format()
                textExpr(
                    Bokmal to inntektstak + " kr",
                    Nynorsk to inntektstak + " kr",
                    English to inntektstak + " NOK"
                )
            }
        }
    }
    title1 {
        text(
            Bokmal to "Reduksjon av barnetillegg for særkullsbarn før skatt",
            Nynorsk to "Reduksjon av barnetillegg for særkullsbarn før skatt",
            English to "Reduction of child supplement payment for children from a previous relationship before tax"
        )
    }
    table(
        header = {
            column(3) {
                text(
                    Bokmal to "Beskrivelse", //TODO
                    Nynorsk to "Beskrivelse", //TODO
                    English to "Description" //TODO
                )
            }
            column(alignment = RIGHT) {
                text(
                    Bokmal to "Beløp", //TODO
                    Nynorsk to "Beløp", //TODO
                    English to "Amount" //TODO
                )
            }
        }
    ) {
        row {
            cell {
                text(
                    Bokmal to "Årlig barnetillegg før reduksjon ut fra inntekt",
                    Nynorsk to "Årleg barnetillegg før reduksjon ut frå inntekt",
                    English to "Yearly child supplement before income reduction"
                )
            }
            cell {
                val belopArForAvkort =
                    argument().map { Kroner(it.belopArForAvkort_barnetilleggSBGjeldende) }
                        .format()
                textExpr(
                    Bokmal to belopArForAvkort + " kr",
                    Nynorsk to belopArForAvkort + " kr",
                    English to belopArForAvkort + " NOK",
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Samlet inntekt brukt i fastsettelse av barnetillegget er",
                    Nynorsk to "Samla inntekt brukt i fastsetjinga av barnetillegget er",
                    English to "Total income applied in calculation of reduction in child supplement is"
                )
            }
            cell {
                val inntektBruktIAvkortning =
                    argument().map { Kroner(it.inntektBruktIAvkortning_barnetilleggSBGjeldende) }
                        .format()
                textExpr(
                    Bokmal to inntektBruktIAvkortning + " kr",
                    Nynorsk to inntektBruktIAvkortning + " kr",
                    English to inntektBruktIAvkortning + " NOK"
                )
            }
        }
        /* IF (
        barnetilleggSBGjeldende.fribelopEllerInntektErPeriodisert == false
        && (
        barnetilleggSBGjeldende.belop != 0
        || barnetilleggSBGjeldende.belop == 0 && barnetilleggSBGjeldende.justeringsbelopAr != 0
        )
        && barnetilleggSBGjeldende.avkortingsbelopAr > 0
        )
        */
        row {
            cell {
                text(
                    Bokmal to "- 50 prosent av inntekt som overstiger fribeløpet",
                    Nynorsk to "- 50 prosent av inntekt som overstig fribeløpet",
                    English to "- 50 percent of income exceeding the allowance amount"
                )
            }
            cell {
                val avkortningsbelopAr =
                    argument().map { Kroner(it.avkortningsbelopAr_barnetilleggSBGjeldende) }
                        .format()
                textExpr(
                    Bokmal to avkortningsbelopAr + " kr",
                    Nynorsk to avkortningsbelopAr + " kr",
                    English to avkortningsbelopAr + " NOK"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "- 50 prosent av inntekt som overstiger fribeløpet (oppgitt som et årlig beløp)",
                    Nynorsk to "- 50 prosent av inntekt som overstig fribeløpet (oppgitt som eit årleg beløp)",
                    English to "- 50 percent of income exceeding the allowance amount (calculated to an annual amount)"
                )
            }
            cell {
                val avkortningsbelopAr =
                    argument().map { Kroner(it.avkortningsbelopAr_barnetilleggSBGjeldende) }
                        .format()
                textExpr(
                    Bokmal to avkortningsbelopAr + " kr",
                    Nynorsk to avkortningsbelopAr + " kr",
                    English to avkortningsbelopAr + " NOK"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "+ Beløp som er brukt for å justere reduksjonen av barnetillegget",
                    Nynorsk to "+ Beløp som er brukt for å justera reduksjonen av barnetillegget",
                    English to "+ Amount which is used to adjust the reduction of child supplement"
                )
            }
            cell {
                val justeringsbelopAr =
                    argument().map { Kroner(it.justeringsbelopAr_barnetilleggSBGjeldende) }
                        .format()
                textExpr(
                    Bokmal to justeringsbelopAr + " kr",
                    Nynorsk to justeringsbelopAr + " kr",
                    English to justeringsbelopAr + " NOK"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "= Årlig barnetillegg etter reduksjon ut fra inntekt",
                    Nynorsk to "Årleg barnetillegg etter reduksjon ut frå inntekt",
                    English to "Yearly child supplement after income reduction"
                )
            }
            cell {
                val belopAr =
                    argument().map { Kroner(it.belopAr_barnetilleggSBGjeldende) }.format()
                textExpr(
                    Bokmal to belopAr + " kr",
                    Nynorsk to belopAr + " kr",
                    English to belopAr + " NOK"
                )
            }
        }
        row {
            cell {
                text(
                    Bokmal to "Utbetaling av barnetillegg per måned",
                    Nynorsk to "Utbetaling av barnetillegg per månad",
                    English to "Child supplement payment for the remaining months of the year"
                )
            }
            cell {
                val belop = argument().map { Kroner(it.belop_barnetilleggSBGjeldende) }.format()
                textExpr(
                    Bokmal to belop + " kr",
                    Nynorsk to belop + " kr",
                    English to belop + " NOK"
                )
            }
        }
    }
}
