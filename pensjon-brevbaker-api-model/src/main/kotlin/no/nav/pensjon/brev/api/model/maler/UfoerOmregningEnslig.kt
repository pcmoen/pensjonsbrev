package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brev.api.model.Institusjon
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sivilstand
import java.time.LocalDate

data class UfoerOmregningEnsligDto(
        val avdod_navn: String,
        val avdod_sivilstand: Sivilstand,
        val barn_overfoert_til_saerkullsbarn: List<String>,
        val barnetillegg_beloep_foer_reduksjon_vedvirk: Number,
        val barnetillegg_er_redusert_mot_tak: Boolean,
        val barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk: Number,
        val barnetillegg_ikke_utbetalt_pga_tak: Boolean,
        val barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk: Number,
        val barnetillegg_saerkullsbarn_beloep_etter_reduksjon_vedvirk: Number,
        val barnetillegg_saerkullsbarn_er_redusert_mot_inntekt_vedvirk: Boolean,
        val barnetillegg_saerkullsbarn_fribeloep_vedvirk: Number,
        val barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk: Number,
        val barnetillegg_saerkullsbarn_inntektstak_vedvirk: Number,
        val bor_i_avtaleland: Boolean,
        val bor_i_norge: Boolean,
        val ektefelletillegg_opphoert: Boolean,
        val eps_bor_sammen_med_bruker_gjeldende: Boolean,
        val eps_institusjon_gjeldende: Institusjon,
        val gjeldende_barnetillegg_saerkullsbarn_er_redusert_mot_inntekt: Boolean,
        val gjeldende_ufoeretrygd_per_maaned_er_inntektsavkortet: Boolean,
        val har_barn_overfoert_til_saerkullsbarn: Boolean,
        val har_barn_som_tidligere_var_saerkullsbarn: Boolean,
        val har_barnetillegg_felles_barn_vedvirk: Boolean,
        val har_barnetillegg_for_saerkullsbarn_vedvirk: Boolean,
        val har_barnetillegg_saerkullsbarn_justeringsbeloep_ar_vedvirk: Boolean,
        val har_barnetillegg_saerkullsbarn_nettobeloep_vedvirk: Boolean,
        val har_barnetillegg_saerkullsbarn_vedvirk: Boolean,
        val har_barnetillegg_vedvirk: Boolean,
        val har_ektefelletillegg_vedvirk: Boolean,
        val har_felles_barn_uten_barnetillegg_med_avdod: Boolean,
        val har_flere_delytelser_i_tillegg_til_ordinaer_ufoeretrygd: Boolean,
        val har_flere_ufoeretrygd_perioder: Boolean,
        val har_minste_inntektsnivaa_foer_ufoeretrygd: Boolean,
        val har_minsteytelse_vedvirk: Boolean,
        val har_ufoeremaaned_vedvirk: Boolean,
        val inntekt_foer_ufoerhet_vedvirk: Number,
        val inntekt_ufoere_endret: Boolean,
        val institusjon_gjeldende: Institusjon,
        val institusjon_vedvirk: Institusjon,
        val kompensasjonsgrad_ufoeretrygd_vedvirk: Number,
        val krav_virkedato_fom: LocalDate,
        val minsteytelse_sats_vedvirk: Number,
        val oppjustert_inntekt_foer_ufoerhet_vedvirk: Number,
        val saktype: Sakstype,
        val sivilstand: Sivilstand,
        val tidligere_saerkullsbarn: List<String>,
        val total_ufoeremaaneder: Number,
        val ufoeretrygd_med_barnetillegg_er_over_95_prosent_av_inntekt_foer_ufoerhet: Boolean,
        val ufoeretrygd_vedvirk_er_inntektsavkortet: Boolean

) {
    constructor() : this(
        avdod_navn = "Avdød Navn",
        avdod_sivilstand = Sivilstand.ENSLIG,
        barn_overfoert_til_saerkullsbarn = listOf("barn1", "barn2", "barn3"),
        barnetillegg_beloep_foer_reduksjon_vedvirk = 123,
        barnetillegg_er_redusert_mot_tak = false,
        barnetillegg_gradert_over_inntekt_foer_ufoer_vedvirk = 123,
        barnetillegg_ikke_utbetalt_pga_tak = false,
        barnetillegg_prosentsats_gradert_over_inntekt_foer_ufoer_vedvirk = 123,
        barnetillegg_saerkullsbarn_beloep_etter_reduksjon_vedvirk = 123,
        barnetillegg_saerkullsbarn_er_redusert_mot_inntekt_vedvirk = false,
        barnetillegg_saerkullsbarn_fribeloep_vedvirk = 123,
        barnetillegg_saerkullsbarn_inntekt_brukt_i_avkortning_vedvirk = 123,
        barnetillegg_saerkullsbarn_inntektstak_vedvirk = 123,
        bor_i_avtaleland = false,
        bor_i_norge = false,
        ektefelletillegg_opphoert = false,
        eps_bor_sammen_med_bruker_gjeldende = false,
        eps_institusjon_gjeldende = Institusjon.INGEN,
        gjeldende_barnetillegg_saerkullsbarn_er_redusert_mot_inntekt = false,
        gjeldende_ufoeretrygd_per_maaned_er_inntektsavkortet = false,
        har_barn_overfoert_til_saerkullsbarn = false,
        har_barn_som_tidligere_var_saerkullsbarn = false,
        har_barnetillegg_felles_barn_vedvirk = false,
        har_barnetillegg_for_saerkullsbarn_vedvirk = false,
        har_barnetillegg_saerkullsbarn_justeringsbeloep_ar_vedvirk = false,
        har_barnetillegg_saerkullsbarn_nettobeloep_vedvirk = false,
        har_barnetillegg_saerkullsbarn_vedvirk = false,
        har_barnetillegg_vedvirk = false,
        har_ektefelletillegg_vedvirk = false,
        har_felles_barn_uten_barnetillegg_med_avdod = false,
        har_flere_delytelser_i_tillegg_til_ordinaer_ufoeretrygd = false,
        har_flere_ufoeretrygd_perioder = false,
        har_minste_inntektsnivaa_foer_ufoeretrygd = false,
        har_minsteytelse_vedvirk = false,
        har_ufoeremaaned_vedvirk = false,
        inntekt_foer_ufoerhet_vedvirk = 123,
        inntekt_ufoere_endret = false,
        institusjon_gjeldende = Institusjon.INGEN,
        institusjon_vedvirk = Institusjon.INGEN,
        kompensasjonsgrad_ufoeretrygd_vedvirk = 123,
        krav_virkedato_fom = LocalDate.of(2020,1,1),
        minsteytelse_sats_vedvirk = 123,
        oppjustert_inntekt_foer_ufoerhet_vedvirk = 123,
        saktype = Sakstype.UFOEREP,
        sivilstand = Sivilstand.ENSLIG,
        tidligere_saerkullsbarn = listOf("saerkullsbarn1", "saerkullsbarn2", "saerkullsbarn3"),
        total_ufoeremaaneder = 123,
        ufoeretrygd_med_barnetillegg_er_over_95_prosent_av_inntekt_foer_ufoerhet = false,
        ufoeretrygd_vedvirk_er_inntektsavkortet = false
    )
}
