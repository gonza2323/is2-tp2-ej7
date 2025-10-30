import { Link } from 'react-router-dom';
import { Divider, Group } from '@mantine/core';
import { ColorSchemeToggler } from '@/components/color-scheme-toggler';
import { Logo } from '@/components/logo';
import { SpotlightSearchBarButton } from '@/components/spotlight-search-bar-button';
import { StickyHeader } from '@/components/sticky-header';
import { CurrentUser } from './current-user';
import { Notifications } from './notifications';
import { SearchMenu } from './search-menu';
import { SidebarButton } from './sidebar-button';
import classes from './header.module.css';

export function Header() {
  return (
    <StickyHeader className={classes.root}>
      <div className={classes.rightContent}>
        <SidebarButton />
        <Link to="/" className={classes.logo}>
          <Logo />
        </Link>
        <SpotlightSearchBarButton placeholder="Search for feature" spotlight={<SearchMenu />} />
      </div>

      <Group>
        <ColorSchemeToggler />
        <Divider orientation='vertical'/>
        <Notifications />
        <CurrentUser />
      </Group>
    </StickyHeader>
  );
}
