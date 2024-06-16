import { SafeHtml } from "@angular/platform-browser";

export interface Toast {
    header: string;
	body: string;
	classname?: string;
	delay?: number;
	headerSvg?: string | SafeHtml;
	autohide?: boolean;
}
